package com.flexbox.backend.cart;

import com.flexbox.backend.cart.model.Cart;
import com.flexbox.backend.cart.model.CartStatus;
import com.flexbox.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserAndStatus(User user, CartStatus status);
}
