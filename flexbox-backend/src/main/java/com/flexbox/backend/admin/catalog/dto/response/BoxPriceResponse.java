package com.flexbox.backend.admin.catalog.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxPrice;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record BoxPriceResponse(
        Long id,
        Long subscriptionBoxId,
        String stripePriceId,
        @JsonProperty("price") BigDecimal amount,
        OffsetDateTime startsAt,
        OffsetDateTime endsAt) {

    public static BoxPriceResponse from(
            SubscriptionBoxPrice price) {

        return new BoxPriceResponse(
                price.getId(),
                price.getSubscriptionBox().getId(),
                price.getStripePriceId(),
                price.getAmount(),
                price.getStartsAt(),
                price.getEndsAt()

        );
    }
}
