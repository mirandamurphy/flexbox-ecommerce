package com.flexbox.backend.catalog.product.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.product.model.Category;
import com.flexbox.backend.catalog.product.model.Product;
import com.flexbox.backend.catalog.product.model.ProductInventory;


public record ProductResponse(
        Long id,
        String name,
        String brand,
        String description,
        @JsonProperty("category") Category category,
        Boolean isActive
) {

    public static ProductResponse from (Product product, CategorySummaryResponse category) {
        return new ProductResponse(product.getId(),
                product.getName(),
                product.getBrand(),
                product.getDescription(),
                product.getCategory(),
                product.getIsActive());

    }

}
