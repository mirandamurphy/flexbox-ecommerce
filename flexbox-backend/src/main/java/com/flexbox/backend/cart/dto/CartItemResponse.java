package com.flexbox.backend.cart.dto;

import com.flexbox.backend.cart.model.CartItem;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        Long subscriptionBoxId,
        String subscriptionBoxName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal
) {
    public static CartItemResponse from(CartItem item) {
        BigDecimal lineTotal = item.getUnitPriceSnapshot().multiply(BigDecimal.valueOf(item.getQuantity()));
        return new CartItemResponse(
                item.getId(),
                item.getSubscriptionBox().getId(),
                item.getSubscriptionBox().getName(),
                item.getQuantity(),
                item.getUnitPriceSnapshot(),
                lineTotal
        );
    }
}
