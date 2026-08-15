package com.flexbox.backend.catalog.box.dto.subscriptionbox;

import com.flexbox.backend.catalog.box.model.SubscriptionBoxPrice;

import java.math.BigDecimal;


public record BoxPriceResponse(
        BigDecimal amount,
        String currency
) {

    public static BoxPriceResponse from (SubscriptionBoxPrice price) {
        return new BoxPriceResponse(price.getAmount(), price.getCurrency());
    }
}
