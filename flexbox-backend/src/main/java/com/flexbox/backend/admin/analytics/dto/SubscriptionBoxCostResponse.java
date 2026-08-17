package com.flexbox.backend.admin.analytics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.admin.analytics.model.SubscriptionBoxCost;

import java.math.BigDecimal;

public record SubscriptionBoxCostResponse(
        Long subscriptionBoxId,
        @JsonProperty("name") String boxName,
        @JsonProperty("cost") BigDecimal boxCost) {

    public static SubscriptionBoxCostResponse from(SubscriptionBoxCost boxCost) {
        return new SubscriptionBoxCostResponse(
                boxCost.getSubscriptionBoxId(),
                boxCost.getBoxName(),
                boxCost.getBoxCost()
        );
    }
}
