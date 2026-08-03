package com.flexbox.backend.catalog.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.dto.category.CategorySummary;
import com.flexbox.backend.catalog.model.Product;

// GET /products (one product)
public record ProductSummary(
        Long id,
        @JsonProperty("category") CategorySummary category,
        String name,
        String description,
        String brand
) {

    public static ProductSummary from(Product product, CategorySummary category) {
        return new ProductSummary(product.getId(),
                category,
                product.getName(),
                product.getDescription(),
                product.getBrand());
    }
}
