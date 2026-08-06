package com.flexbox.backend.cart.dto;

public record AddCartItemRequest(Long subscriptionBoxId, int quantity) {
}
