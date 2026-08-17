package com.flexbox.backend.admin.catalog.dto.response;

import com.flexbox.backend.catalog.box.model.SubscriptionBoxProduct;

public record AdminBoxProductResponse(
        Long subscriptionBoxId,
        Long productId,
        String productName,
        Integer quantity
) {
    public static AdminBoxProductResponse from(SubscriptionBoxProduct boxProduct) {
        var product = boxProduct.getProduct();

        return new AdminBoxProductResponse(
                boxProduct.getSubscriptionBox().getId(),
                product.getId(),
                product.getName(),
                boxProduct.getQuantity()
        );
    }
}