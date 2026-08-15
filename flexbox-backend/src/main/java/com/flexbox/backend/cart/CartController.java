package com.flexbox.backend.cart;

import com.flexbox.backend.cart.dto.AddCartItemRequest;
import com.flexbox.backend.cart.dto.CartItemResponse;
import com.flexbox.backend.cart.dto.CartResponse;
import com.flexbox.backend.cart.dto.UpdateCartItemRequest;
import com.flexbox.backend.cart.model.Cart;
import com.flexbox.backend.cart.model.CartItem;
import com.flexbox.backend.catalog.model.SubscriptionBox;
import com.flexbox.backend.catalog.repository.SubscriptionBoxRepository;
import com.flexbox.backend.repository.UserRepository;
import com.flexbox.backend.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * userId is taken as a request parameter for now instead of the
 * authenticated principal. There is no JWT security filter chain wired
 * up yet to populate the request's authenticated user, so this is a
 * temporary stand in until that is in place.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final SubscriptionBoxRepository subscriptionBoxRepository;

    public CartController(CartService cartService,
                           CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           UserRepository userRepository,
                           SubscriptionBoxRepository subscriptionBoxRepository) {
        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.subscriptionBoxRepository = subscriptionBoxRepository;
    }

    @GetMapping
    public CartResponse getCart(@RequestParam Long userId) {
        User user = findUser(userId);
        Cart cart = cartService.getOrCreateActiveCart(user);
        return toResponse(cart);
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(@RequestParam Long userId,
                                                 @RequestBody AddCartItemRequest request) {
        User user = findUser(userId);
        SubscriptionBox box = subscriptionBoxRepository.findById(request.subscriptionBoxId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Subscription box not found: " + request.subscriptionBoxId()));

        cartService.addItem(user, box, request.quantity());

        Cart cart = cartService.getOrCreateActiveCart(user);
        return ResponseEntity.ok(toResponse(cart));
    }

    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateQuantity(@PathVariable Long cartItemId,
                                                             @RequestBody UpdateCartItemRequest request) {
        CartItem updated = cartService.updateQuantity(cartItemId, request.quantity());
        return ResponseEntity.ok(CartItemResponse.from(updated));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    private CartResponse toResponse(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCart(cart);
        List<CartItemResponse> itemResponses = items.stream()
                .map(CartItemResponse::from)
                .toList();
        var total = cartService.calculateTotal(cart);
        return new CartResponse(cart.getId(), itemResponses, total);
    }
}
