package com.flexbox.backend.admin.catalog.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxPrice;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record AdminBoxPriceResponse(
        Long id,
        Long subscriptionBoxId,
        @JsonProperty("price") BigDecimal amount,
        OffsetDateTime startsAt,
        OffsetDateTime endsAt) {

    public static AdminBoxPriceResponse from(
            SubscriptionBoxPrice price) {

        return new AdminBoxPriceResponse(
                price.getId(),
                price.getSubscriptionBox().getId(),
                price.getAmount(),
                price.getStartsAt(),
                price.getEndsAt()

        );
    }
}
