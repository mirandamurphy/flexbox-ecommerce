package com.flexbox.backend.cart;

import com.flexbox.backend.catalog.SubscriptionBox;
import com.flexbox.backend.catalog.SubscriptionBoxPrice;
import com.flexbox.backend.catalog.SubscriptionBoxPriceRepository;
import com.flexbox.backend.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final SubscriptionBoxPriceRepository priceRepository;

    public CartService(CartRepository cartRepository,
                        CartItemRepository cartItemRepository,
                        SubscriptionBoxPriceRepository priceRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.priceRepository = priceRepository;
    }

    public Cart getOrCreateActiveCart(User user) {
        return cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setStatus(CartStatus.ACTIVE);
                    cart.setCreatedAt(OffsetDateTime.now());
                    cart.setUpdatedAt(OffsetDateTime.now());
                    return cartRepository.save(cart);
                });
    }

    public CartItem addItem(User user, SubscriptionBox subscriptionBox, int quantity) {
        Cart cart = getOrCreateActiveCart(user);

        int existingQuantity = cartItemRepository.findByCartAndSubscriptionBox(cart, subscriptionBox)
                .map(CartItem::getQuantity)
                .orElse(0);
        int requestedTotal = existingQuantity + quantity;

        Integer availableUnits = subscriptionBox.getAvailableUnits();
        if (availableUnits != null && requestedTotal > availableUnits) {
            throw new InsufficientStockException(
                    "Only " + availableUnits + " unit(s) available for " + subscriptionBox.getName()
                            + ", requested " + requestedTotal + " total");
        }

        BigDecimal currentPrice = priceRepository.findCurrentPrice(subscriptionBox.getId(), OffsetDateTime.now())
                .map(SubscriptionBoxPrice::getAmount)
                .orElseThrow(() -> new IllegalStateException(
                        "No active price found for subscription box " + subscriptionBox.getId()));

        return cartItemRepository.findByCartAndSubscriptionBox(cart, subscriptionBox)
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + quantity);
                    existing.setUpdatedAt(OffsetDateTime.now());
                    return cartItemRepository.save(existing);
                })
                .orElseGet(() -> {
                    CartItem item = new CartItem();
                    item.setCart(cart);
                    item.setSubscriptionBox(subscriptionBox);
                    item.setQuantity(quantity);
                    item.setUnitPriceSnapshot(currentPrice);
                    item.setAddedAt(OffsetDateTime.now());
                    item.setUpdatedAt(OffsetDateTime.now());
                    return cartItemRepository.save(item);
                });
    }

    public void removeItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public CartItem updateQuantity(Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found: " + cartItemId));
        item.setQuantity(quantity);
        item.setUpdatedAt(OffsetDateTime.now());
        return cartItemRepository.save(item);
    }

    public BigDecimal calculateTotal(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCart(cart);
        return items.stream()
                .map(item -> item.getUnitPriceSnapshot().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
