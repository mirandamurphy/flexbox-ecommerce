package com.flexbox.backend.order;

import com.flexbox.backend.cart.CartItemRepository;
import com.flexbox.backend.cart.model.Cart;
import com.flexbox.backend.cart.model.CartItem;
import com.flexbox.backend.catalog.model.SubscriptionBox;
import com.flexbox.backend.payment.Payment;
import com.flexbox.backend.payment.PaymentRepository;
import com.flexbox.backend.payment.PaymentStatus;
import com.flexbox.backend.user.User;
import com.stripe.model.checkout.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * Covers the actual success-path business logic in CheckoutService, not just
 * the guard clauses already covered in CheckoutServiceRetryValidationTest.
 * The real Stripe API call inside createStripeSession is mocked statically,
 * this never touches the network, same limitation the original service test
 * was built under, just resolved this time instead of left as a gap.
 */
@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CheckoutSessionRepository checkoutSessionRepository;

    @Mock
    private Session fakeStripeSession;

    private MockedStatic<Session> stripeSessionStatic;
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        checkoutService = new CheckoutService(
                cartItemRepository,
                orderRepository,
                orderItemRepository,
                paymentRepository,
                checkoutSessionRepository,
                "http://localhost:3000/checkout/success",
                "http://localhost:3000/checkout/cancel");

        stripeSessionStatic = mockStatic(Session.class);

        // Repositories just echo back whatever gets saved, with an id set,
        // same pattern JPA itself would produce. Marked lenient since not
        // every test in this class touches every repository (the empty-cart
        // guard clause test never gets this far).
        lenient().when(orderRepository.save(any())).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            if (o.getId() == null) {
                o.setId(1L);
            }
            return o;
        });
        lenient().when(orderItemRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        lenient().when(paymentRepository.save(any())).thenAnswer(inv -> {
            Payment p = inv.getArgument(0);
            p.setId(10L);
            return p;
        });
        lenient().when(checkoutSessionRepository.save(any())).thenAnswer(inv -> {
            CheckoutSession cs = inv.getArgument(0);
            cs.setId(20L);
            return cs;
        });
    }

    @AfterEach
    void tearDown() {
        stripeSessionStatic.close();
    }

    private CartItem buildCartItem(String boxName, BigDecimal price, int quantity) {
        SubscriptionBox box = new SubscriptionBox();
        box.setName(boxName);

        CartItem item = new CartItem();
        item.setSubscriptionBox(box);
        item.setUnitPriceSnapshot(price);
        item.setQuantity(quantity);
        return item;
    }

    @Test
    void createCheckoutSession_buildsOrderPaymentAndSession_whenCartHasItems() throws Exception {
        User user = new User();
        Cart cart = new Cart();

        CartItem item = buildCartItem("Essential Fitness Box", new BigDecimal("29.99"), 2);
        when(cartItemRepository.findByCart(cart)).thenReturn(List.of(item));

        when(fakeStripeSession.getId()).thenReturn("cs_test_fake");
        when(fakeStripeSession.getUrl()).thenReturn("https://checkout.stripe.com/c/pay/fake");
        stripeSessionStatic.when(() -> Session.create(any(com.stripe.param.checkout.SessionCreateParams.class)))
                .thenReturn(fakeStripeSession);

        CheckoutService.CheckoutResult result = checkoutService.createCheckoutSession(user, cart);

        assertThat(result.checkoutUrl()).isEqualTo("https://checkout.stripe.com/c/pay/fake");
        assertThat(result.checkoutSession().getStatus()).isEqualTo(CheckoutSessionStatus.OPEN);
        assertThat(result.checkoutSession().getPayment().getStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(result.checkoutSession().getPayment().getOrder().getTotalAmount())
                .isEqualByComparingTo("59.98");
        assertThat(result.checkoutSession().getPayment().getOrder().getStatus())
                .isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void createCheckoutSession_throwsIllegalState_whenCartIsEmpty() {
        User user = new User();
        Cart cart = new Cart();

        when(cartItemRepository.findByCart(cart)).thenReturn(List.of());

        assertThatThrownBy(() -> checkoutService.createCheckoutSession(user, cart))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("empty cart");
    }

    @Test
    void retryCheckout_rebuildsSessionFromOriginalOrderItems_whenOrderCancelled() throws Exception {
        User user = new User();
        Order order = new Order();
        order.setId(5L);
        order.setUser(user);
        order.setStatus(OrderStatus.CANCELLED);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setSubscriptionBoxNameSnapshot("Elite Athlete Box");
        orderItem.setPurchasePriceSnapshot(new BigDecimal("99.99"));
        orderItem.setQuantity(1);

        when(orderItemRepository.findByOrder(order)).thenReturn(List.of(orderItem));

        when(fakeStripeSession.getId()).thenReturn("cs_test_retry");
        when(fakeStripeSession.getUrl()).thenReturn("https://checkout.stripe.com/c/pay/retry");
        stripeSessionStatic.when(() -> Session.create(any(com.stripe.param.checkout.SessionCreateParams.class)))
                .thenReturn(fakeStripeSession);

        CheckoutService.CheckoutResult result = checkoutService.retryCheckout(order);

        assertThat(result.checkoutUrl()).isEqualTo("https://checkout.stripe.com/c/pay/retry");
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.checkoutSession().getPayment().getOrder().getTotalAmount())
                .isEqualByComparingTo("99.99");
    }
}
