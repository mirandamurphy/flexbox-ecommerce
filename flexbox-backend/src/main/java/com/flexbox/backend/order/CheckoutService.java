package com.flexbox.backend.order;

import com.flexbox.backend.cart.Cart;
import com.flexbox.backend.cart.CartItem;
import com.flexbox.backend.cart.CartItemRepository;
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
@Transactional
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

    public CheckoutSession createCheckoutSession(User user, Cart cart) throws StripeException {
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty cart");
        }

        BigDecimal total = cartItems.stream()
                .map(item -> item.getUnitPriceSnapshot().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setCurrency("CAD");
        order.setTotalAmount(total);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(OffsetDateTime.now());
        order.setUpdatedAt(OffsetDateTime.now());
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

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(total);
        payment.setCurrency("CAD");
        payment.setStatus(PaymentStatus.PENDING);
        payment = paymentRepository.save(payment);

        Session stripeSession = createStripeSession(cartItems, order.getId());

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
        checkoutSession.setCreatedAt(OffsetDateTime.now());
        checkoutSession.setUpdatedAt(OffsetDateTime.now());

        return checkoutSessionRepository.save(checkoutSession);
    }

    private Session createStripeSession(List<CartItem> cartItems, Long orderId) throws StripeException {
        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?order_id=" + orderId)
                .setCancelUrl(cancelUrl + "?order_id=" + orderId);

        for (CartItem item : cartItems) {
            long unitAmountCents = item.getUnitPriceSnapshot()
                    .multiply(BigDecimal.valueOf(100))
                    .longValueExact();

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity((long) item.getQuantity())
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("cad")
                                    .setUnitAmount(unitAmountCents)
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(item.getSubscriptionBox().getName())
                                                    .build())
                                    .build())
                    .build();

            paramsBuilder.addLineItem(lineItem);
        }

        return Session.create(paramsBuilder.build());
    }
}
