package com.flexbox.backend.cart;

import com.flexbox.backend.cart.model.Cart;
import com.flexbox.backend.cart.model.CartItem;
import com.flexbox.backend.cart.model.CartStatus;
import com.flexbox.backend.catalog.box.model.SubscriptionBox;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxPrice;
import com.flexbox.backend.catalog.box.repository.SubscriptionBoxPriceRepository;
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
                    return cartRepository.save(cart);
                });
    }

    public CartItem addItem(User user, SubscriptionBox subscriptionBox, int quantity) {
        Cart cart = getOrCreateActiveCart(user);

        var existingItem = cartItemRepository.findByCartAndSubscriptionBox(cart, subscriptionBox);

        int existingQuantity = existingItem.map(CartItem::getQuantity).orElse(0);
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

        return existingItem
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + quantity);
                    return cartItemRepository.save(existing);
                })
                .orElseGet(() -> {
                    CartItem item = new CartItem();
                    item.setCart(cart);
                    item.setSubscriptionBox(subscriptionBox);
                    item.setQuantity(quantity);
                    item.setUnitPriceSnapshot(currentPrice);
                    return cartItemRepository.save(item);
                });
    }

    public void removeItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public CartItem updateQuantity(Long cartItemId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException(
                    "Quantity must be greater than zero, use removeItem to delete a cart item");
        }

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found: " + cartItemId));

        SubscriptionBox subscriptionBox = item.getSubscriptionBox();
        Integer availableUnits = subscriptionBox.getAvailableUnits();
        if (availableUnits != null && quantity > availableUnits) {
            throw new InsufficientStockException(
                    "Only " + availableUnits + " unit(s) available for " + subscriptionBox.getName()
                            + ", requested " + quantity);
        }

        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    public BigDecimal calculateTotal(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCart(cart);
        return items.stream()
                .map(item -> item.getUnitPriceSnapshot().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
