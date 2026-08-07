package com.flexbox.backend.order;

import com.flexbox.backend.cart.CartItemRepository;
import com.flexbox.backend.cart.model.Cart;
import com.flexbox.backend.cart.model.CartItem;
import com.flexbox.backend.payment.Payment;
import com.flexbox.backend.payment.PaymentRepository;
import com.flexbox.backend.payment.PaymentStatus;
import com.flexbox.backend.user.User;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional(rollbackFor = StripeException.class)
public class CheckoutService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final CheckoutSessionRepository checkoutSessionRepository;
    private final String successUrl;
    private final String cancelUrl;

    public CheckoutService(CartItemRepository cartItemRepository,
                            OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            PaymentRepository paymentRepository,
                            CheckoutSessionRepository checkoutSessionRepository,
                            @Value("${app.checkout.success-url}") String successUrl,
                            @Value("${app.checkout.cancel-url}") String cancelUrl) {
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
        this.checkoutSessionRepository = checkoutSessionRepository;
        this.successUrl = successUrl;
        this.cancelUrl = cancelUrl;
    }

    public CheckoutResult createCheckoutSession(User user, Cart cart) throws StripeException {
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty cart");
        }

        List<LineItemData> lineItems = cartItems.stream()
                .map(item -> new LineItemData(
                        item.getSubscriptionBox().getName(),
                        item.getUnitPriceSnapshot(),
                        item.getQuantity()))
                .toList();

        BigDecimal total = sumTotal(lineItems);

        Order order = new Order();
        order.setUser(user);
        order.setCurrency("CAD");
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.PENDING);
        order = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setSubscriptionBox(cartItem.getSubscriptionBox());
            orderItem.setSubscriptionBoxNameSnapshot(cartItem.getSubscriptionBox().getName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPurchasePriceSnapshot(cartItem.getUnitPriceSnapshot());
            orderItemRepository.save(orderItem);
        }

        return buildPaymentAndSession(user, order, total, lineItems);
    }

    /**
     * Starts a new Stripe Checkout Session for an order whose previous session
     * expired or failed, without making the customer rebuild their cart.
     * Reuses the original order and its line items.
     */
    public CheckoutResult retryCheckout(Order order) throws StripeException {
        if (order.getStatus() != OrderStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Only cancelled orders can be retried, current status: " + order.getStatus());
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
        if (orderItems.isEmpty()) {
            throw new IllegalStateException("Order " + order.getId() + " has no items to retry");
        }

        List<LineItemData> lineItems = orderItems.stream()
                .map(item -> new LineItemData(
                        item.getSubscriptionBoxNameSnapshot(),
                        item.getPurchasePriceSnapshot(),
                        item.getQuantity()))
                .toList();

        order.setStatus(OrderStatus.PENDING);
        order = orderRepository.save(order);

        BigDecimal total = sumTotal(lineItems);

        return buildPaymentAndSession(order.getUser(), order, total, lineItems);
    }

    private CheckoutResult buildPaymentAndSession(User user, Order order, BigDecimal total,
                                                     List<LineItemData> lineItems) throws StripeException {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(total);
        payment.setCurrency("CAD");
        payment.setStatus(PaymentStatus.PENDING);
        payment = paymentRepository.save(payment);

        Session stripeSession = createStripeSession(lineItems, order.getId());

        CheckoutSession checkoutSession = new CheckoutSession();
        checkoutSession.setUser(user);
        checkoutSession.setStripeSessionId(stripeSession.getId());
        checkoutSession.setPayment(payment);
        checkoutSession.setMode(CheckoutSessionMode.PAYMENT);
        checkoutSession.setStatus(CheckoutSessionStatus.OPEN);
        checkoutSession.setAmountSubtotal(total);
        checkoutSession.setAmountTotal(total);
        checkoutSession.setCurrency("CAD");
        checkoutSession.setSuccessUrl(successUrl);
        checkoutSession.setCancelUrl(cancelUrl);

        checkoutSession = checkoutSessionRepository.save(checkoutSession);
        return new CheckoutResult(checkoutSession, stripeSession.getUrl());
    }

    private BigDecimal sumTotal(List<LineItemData> lineItems) {
        return lineItems.stream()
                .map(item -> item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Session createStripeSession(List<LineItemData> lineItems, Long orderId) throws StripeException {
        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?order_id=" + orderId)
                .setCancelUrl(cancelUrl + "?order_id=" + orderId);

        for (LineItemData item : lineItems) {
            long unitAmountCents = item.unitPrice()
                    .multiply(BigDecimal.valueOf(100))
                    .longValueExact();

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity((long) item.quantity())
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("cad")
                                    .setUnitAmount(unitAmountCents)
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(item.name())
                                                    .build())
                                    .build())
                    .build();

            paramsBuilder.addLineItem(lineItem);
        }

        return Session.create(paramsBuilder.build());
    }

    private record LineItemData(String name, BigDecimal unitPrice, int quantity) {
    }

    public record CheckoutResult(CheckoutSession checkoutSession, String checkoutUrl) {
    }
}
