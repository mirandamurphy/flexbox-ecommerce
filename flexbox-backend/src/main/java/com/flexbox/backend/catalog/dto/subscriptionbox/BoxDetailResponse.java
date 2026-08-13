package com.flexbox.backend.catalog.dto.subscriptionbox;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.box.model.SubscriptionBox;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxPrice;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxProduct;

import java.util.List;

public record BoxDetailResponse(
        Long id,
        String name,
        String description,
        @JsonProperty("imageUrl") String imageUrl,
        @JsonProperty("price") BoxPriceResponse price,
        @JsonProperty("products") List<BoxProductResponse> products
) {

    public static BoxDetailResponse from (SubscriptionBox box,
                                          SubscriptionBoxPrice price,
                                          List<SubscriptionBoxProduct> products) {

        return new BoxDetailResponse(
                box.getId(),
                box.getName(),
                box.getDescription(),
                box.getImageFile(),
                BoxPriceResponse.from(price),
                products.stream()
                        .map(BoxProductResponse::from)
                        .toList()
        );

    }
}
