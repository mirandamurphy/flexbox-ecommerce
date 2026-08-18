package com.flexbox.backend.admin.analytics.dto;

import com.flexbox.backend.admin.analytics.model.SubscriptionBoxProductCost;

import java.math.BigDecimal;

public record SubscriptionBoxProductCostResponse(
        Long subscriptionBoxId,
        Long productId,
        String boxName,
        String brand,
        String productName,
        Long categoryId,
        String categoryName,
        Integer quantity,
        BigDecimal productCost) {

    public static SubscriptionBoxProductCostResponse from(SubscriptionBoxProductCost boxProductCost) {
        return new SubscriptionBoxProductCostResponse(
                boxProductCost.getId().getSubscriptionBoxId(),
                boxProductCost.getId().getProductId(),
                boxProductCost.getBoxName(),
                boxProductCost.getBrand(),
                boxProductCost.getProductName(),
                boxProductCost.getCategoryId(),
                boxProductCost.getCategoryName(),
                boxProductCost.getQuantity(),
                boxProductCost.getProductCost()
        );
    }
}
