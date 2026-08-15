package com.flexbox.backend.catalog.box.dto.subscriptionbox;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.box.model.SubscriptionBox;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxPrice;

import java.math.BigDecimal;


public record BoxResponse(
        Long id,
        String name,
        String description,
        String imageUrl,
        BigDecimal price,
        String currency,
        @JsonProperty("active") Boolean isActive
) {

    public static BoxResponse from (SubscriptionBox box,
                                    SubscriptionBoxPrice price
                                          ) {

        return new BoxResponse(
                box.getId(),
                box.getName(),
                box.getDescription(),
                box.getImageFile(),
                price.getAmount(),
                price.getCurrency(),
                box.getIsActive()
        );

    }
}
