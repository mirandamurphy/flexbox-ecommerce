package com.flexbox.backend.catalog.product.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.product.model.Product;


public record ProductResponse(
        Long id,
        String name,
        String brand,
        String description,
        @JsonProperty("category") CategorySummaryResponse category,
        Boolean isActive
) {

    public static ProductResponse from (Product product, CategorySummaryResponse category) {
        return new ProductResponse(product.getId(),
                product.getName(),
                product.getBrand(),
                product.getDescription(),
                category,
                product.getIsActive());

    }
}
