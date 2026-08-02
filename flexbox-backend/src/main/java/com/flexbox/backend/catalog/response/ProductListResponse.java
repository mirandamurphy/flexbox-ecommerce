package com.flexbox.backend.catalog.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.dto.product.ProductSummary;
import com.flexbox.backend.catalog.dto.subscriptionbox.SubscriptionBoxProductSummary;

import java.util.List;

public record ProductListResponse(
        @JsonProperty("data") List<ProductSummary> products
) {
}
