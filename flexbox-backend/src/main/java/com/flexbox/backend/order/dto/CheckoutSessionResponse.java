package com.flexbox.backend.order.dto;

public record CheckoutSessionResponse(Long orderId, String checkoutUrl) {
}
