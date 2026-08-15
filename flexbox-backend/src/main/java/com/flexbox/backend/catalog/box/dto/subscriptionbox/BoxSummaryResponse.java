package com.flexbox.backend.catalog.box.dto.subscriptionbox;

import com.flexbox.backend.catalog.box.model.SubscriptionBox;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxPrice;

public record BoxSummaryResponse(
        Long id,
        String name,
        String description,
        BoxPriceResponse price
) {
    public static BoxSummaryResponse from(
            SubscriptionBox box, SubscriptionBoxPrice price) {
        return new BoxSummaryResponse(
                box.getId(),
                box.getName(),
                box.getDescription(),
                BoxPriceResponse.from(price));
    }
}
