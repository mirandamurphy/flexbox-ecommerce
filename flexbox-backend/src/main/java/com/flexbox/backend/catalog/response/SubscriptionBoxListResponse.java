package com.flexbox.backend.catalog.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.dto.subscriptionbox.SubscriptionBoxSummary;

import java.util.List;

public record SubscriptionBoxListResponse(
        @JsonProperty("data")List<SubscriptionBoxSummary> subscriptionBoxes
        ) {
}
