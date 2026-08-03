package com.flexbox.backend.catalog.dto.subscriptionbox;

import com.flexbox.backend.catalog.model.SubscriptionBoxPrice;

import java.math.BigDecimal;

// Used to get summary of the Subscription Box price
public record SubscriptionBoxPriceSummary(
        BigDecimal amount,
        String currency
) {

    public static SubscriptionBoxPriceSummary from (SubscriptionBoxPrice price) {
        return new SubscriptionBoxPriceSummary(price.getAmount(), price.getCurrency());
    }
}
