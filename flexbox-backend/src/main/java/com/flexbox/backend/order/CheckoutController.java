package com.flexbox.backend.order;

import com.flexbox.backend.cart.CartRepository;
import com.flexbox.backend.cart.model.Cart;
import com.flexbox.backend.cart.model.CartStatus;
import com.flexbox.backend.order.dto.CheckoutSessionResponse;
import com.flexbox.backend.user.repository.UserRepository;
import com.flexbox.backend.user.User;
import com.stripe.exception.StripeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * userId is taken as a request parameter for now instead of the
 * authenticated principal. There is no JWT security filter chain wired
 * up yet to populate the request's authenticated user, so this is a
 * temporary stand in until that is in place.
 */
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public CheckoutController(CheckoutService checkoutService,
                               CartRepository cartRepository,
                               OrderRepository orderRepository,
                               UserRepository userRepository) {
        this.checkoutService = checkoutService;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<CheckoutSessionResponse> createCheckoutSession(@RequestParam Long userId)
            throws StripeException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Cart cart = cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("No active cart for user: " + userId));

        CheckoutService.CheckoutResult result = checkoutService.createCheckoutSession(user, cart);

        Long orderId = result.checkoutSession().getPayment().getOrder().getId();
        return ResponseEntity.ok(new CheckoutSessionResponse(orderId, result.checkoutUrl()));
    }

    @PostMapping("/{orderId}/retry")
    public ResponseEntity<CheckoutSessionResponse> retryCheckout(@PathVariable Long orderId)
            throws StripeException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        CheckoutService.CheckoutResult result = checkoutService.retryCheckout(order);

        return ResponseEntity.ok(new CheckoutSessionResponse(orderId, result.checkoutUrl()));
    }
}
