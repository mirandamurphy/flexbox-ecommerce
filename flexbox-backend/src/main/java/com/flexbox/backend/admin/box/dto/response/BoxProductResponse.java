package com.flexbox.backend.admin.box.dto.response;

import com.flexbox.backend.catalog.box.model.SubscriptionBoxProduct;

public record BoxProductResponse(
        Long subscriptionBoxId,
        Long productId,
        String productName,
        Integer quantity
) {

    public static BoxProductResponse from(
            SubscriptionBoxProduct boxProduct) {

        return new BoxProductResponse(
                boxProduct.getSubscriptionBox().getId(),
                boxProduct.getProduct().getId(),
                boxProduct.getProduct().getName(),
                boxProduct.getQuantity()
        );
    }
}
