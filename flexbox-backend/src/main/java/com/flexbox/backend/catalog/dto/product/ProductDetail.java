package com.flexbox.backend.catalog.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.dto.category.CategorySummary;
import com.flexbox.backend.catalog.model.Product;

// GET products/{id}
public record ProductDetail(
        Long id,
        String name,
        String brand,
        String description,
        @JsonProperty("category") CategorySummary category,
        Boolean isActive
) {

    public static ProductDetail from (Product product, CategorySummary category) {
        return new ProductDetail(product.getId(),
                product.getName(),
                product.getBrand(),
                product.getDescription(),
                category,
                product.getIsActive());

    }
}
