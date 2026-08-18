package com.flexbox.backend.order;

import com.flexbox.backend.cart.CartRepository;
import com.flexbox.backend.cart.model.Cart;
import com.flexbox.backend.cart.model.CartStatus;
import com.flexbox.backend.payment.Payment;
import com.flexbox.backend.user.repository.UserRepository;
import com.flexbox.backend.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CheckoutController.class)
class CheckoutControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @MockitoBean
    CheckoutService checkoutService;

    @MockitoBean
    CartRepository cartRepository;

    @MockitoBean
    OrderRepository orderRepository;

    @MockitoBean
    UserRepository userRepository;

    private User buildUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setEmail("controllertest@flexbox.ca");
        return user;
    }

    private CheckoutService.CheckoutResult buildResult(Long orderId, String url) {
        Order order = new Order();
        order.setId(orderId);

        Payment payment = new Payment();
        payment.setOrder(order);

        CheckoutSession session = new CheckoutSession();
        session.setPayment(payment);

        return new CheckoutService.CheckoutResult(session, url);
    }

    @Test
    void createCheckoutSession_shouldReturn200_whenUserAndCartExist() throws Exception {
        User user = buildUser(1L);
        Cart cart = new Cart();
        cart.setId(5L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
        when(checkoutService.createCheckoutSession(any(), any()))
                .thenReturn(buildResult(42L, "https://checkout.stripe.com/c/pay/fake"));

        var result = mockMvcTester
                .post()
                .uri("/api/checkout?userId=1")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .extractingPath("$.orderId")
                .isEqualTo(42);

        assertThat(result)
                .bodyJson()
                .extractingPath("$.checkoutUrl")
                .isEqualTo("https://checkout.stripe.com/c/pay/fake");
    }

    @Test
    void createCheckoutSession_shouldReturn404_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        MvcTestResult result = mockMvcTester
                .post()
                .uri("/api/checkout?userId=99")
                .exchange();

        assertThat(result).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void createCheckoutSession_shouldReturn409_whenNoActiveCart() {
        User user = buildUser(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)).thenReturn(Optional.empty());

        MvcTestResult result = mockMvcTester
                .post()
                .uri("/api/checkout?userId=1")
                .exchange();

        assertThat(result).hasStatus(HttpStatus.CONFLICT);
    }

    @Test
    void retryCheckout_shouldReturn200_whenOrderExists() throws Exception {
        Order order = new Order();
        order.setId(7L);

        when(orderRepository.findById(7L)).thenReturn(Optional.of(order));
        when(checkoutService.retryCheckout(order))
                .thenReturn(buildResult(7L, "https://checkout.stripe.com/c/pay/retry"));

        var result = mockMvcTester
                .post()
                .uri("/api/checkout/7/retry")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .extractingPath("$.orderId")
                .isEqualTo(7);
    }

    @Test
    void retryCheckout_shouldReturn404_whenOrderNotFound() {
        when(orderRepository.findById(123L)).thenReturn(Optional.empty());

        MvcTestResult result = mockMvcTester
                .post()
                .uri("/api/checkout/123/retry")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND)
                .bodyJson()
                .extractingPath("$.title")
                .isEqualTo("Order Not Found");
    }
}
