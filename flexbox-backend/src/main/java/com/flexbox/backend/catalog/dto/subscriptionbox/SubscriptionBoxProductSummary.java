package com.flexbox.backend.catalog.dto.subscriptionbox;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.entity.SubscriptionBoxProduct;

// Used to get product details for the subscription box
public record SubscriptionBoxProductSummary(
       @JsonProperty("productId") Long id,
       @JsonProperty("productName") String name,
       @JsonProperty("brand") String brand,
       @JsonProperty("quantity") Integer quantity
) {

    public static SubscriptionBoxProductSummary from (SubscriptionBoxProduct subscriptionBoxProduct) {
        var product = subscriptionBoxProduct.getProduct();
        return new SubscriptionBoxProductSummary(
                product.getId(),
                product.getName(),
                product.getBrand(),
                subscriptionBoxProduct.getQuantity()
        );
    }
}
