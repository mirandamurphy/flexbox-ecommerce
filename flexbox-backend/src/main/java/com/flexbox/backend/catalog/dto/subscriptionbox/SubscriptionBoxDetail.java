package com.flexbox.backend.catalog.dto.subscriptionbox;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.entity.SubscriptionBox;
import com.flexbox.backend.catalog.entity.SubscriptionBoxPrice;
import com.flexbox.backend.catalog.entity.SubscriptionBoxProduct;

import java.util.List;

// GET /subscription-boxes/{id}
// Full detail of 1 box
public record SubscriptionBoxDetail(
        Long id,
        String name,
        String description,
        @JsonProperty("price") SubscriptionBoxPriceSummary price,
        @JsonProperty("products") List<SubscriptionBoxProductSummary> products
) {

    public static SubscriptionBoxDetail from (SubscriptionBox subscriptionBox,
                                              SubscriptionBoxPrice price,
                                              List<SubscriptionBoxProduct> products) {

        return new SubscriptionBoxDetail(
                subscriptionBox.getId(),
                subscriptionBox.getName(),
                subscriptionBox.getDescription(),
                SubscriptionBoxPriceSummary.from(price),
                products.stream()
                        .map(SubscriptionBoxProductSummary::from)
                        .toList()
        );


    }
}
