package com.flexbox.backend.cart;

import com.flexbox.backend.cart.model.Cart;
import com.flexbox.backend.cart.model.CartItem;
import com.flexbox.backend.catalog.model.SubscriptionBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);
    Optional<CartItem> findByCartAndSubscriptionBox(Cart cart, SubscriptionBox subscriptionBox);
}
