package com.flexbox.backend.catalog.dto.subscriptionbox;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.entity.SubscriptionBox;
import com.flexbox.backend.catalog.entity.SubscriptionBoxPrice;

import java.util.List;

// GET subscription-boxes (one box)
public record SubscriptionBoxSummary(
        Long id,
        String name,
        SubscriptionBoxPriceSummary price
) {
    public static SubscriptionBoxSummary from(
            SubscriptionBox subscriptionBox, SubscriptionBoxPrice price) {
        return new SubscriptionBoxSummary(
                subscriptionBox.getId(),
                subscriptionBox.getName(),
                SubscriptionBoxPriceSummary.from(price));


}
