package com.flexbox.backend.order;

import com.flexbox.backend.cart.CartItemRepository;
import com.flexbox.backend.payment.PaymentRepository;
import com.flexbox.backend.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * Covers the retryCheckout guard clauses only. The actual Stripe Checkout
 * Session creation is not covered here, the same limitation applies as for
 * createCheckoutSession, since that requires a live Stripe call.
 */
@ExtendWith(MockitoExtension.class)
class CheckoutServiceRetryValidationTest {

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

    private CheckoutService checkoutService;

    private CheckoutService buildService() {
        return new CheckoutService(
                cartItemRepository,
                orderRepository,
                orderItemRepository,
                paymentRepository,
                checkoutSessionRepository,
                "http://localhost:3000/checkout/success",
                "http://localhost:3000/checkout/cancel");
    }

    @Test
    void retryCheckout_onNonCancelledOrder_throwsIllegalState() {
        checkoutService = buildService();

        Order order = new Order();
        order.setId(1L);
        order.setUser(new User());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("10.00"));

        assertThatThrownBy(() -> checkoutService.retryCheckout(order))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("cancelled");
    }

    @Test
    void retryCheckout_onCancelledOrderWithNoItems_throwsIllegalState() {
        checkoutService = buildService();

        Order order = new Order();
        order.setId(2L);
        order.setUser(new User());
        order.setStatus(OrderStatus.CANCELLED);
        order.setTotalAmount(new BigDecimal("10.00"));

        lenient().when(orderItemRepository.findByOrder(order)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> checkoutService.retryCheckout(order))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("no items");
    }
}
