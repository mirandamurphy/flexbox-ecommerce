package com.flexbox.backend.catalog.dto.product;

import com.flexbox.backend.catalog.dto.category.CategorySummary;
import com.flexbox.backend.catalog.entity.Product;

public record ProductDetail(
        Long id,
        String name,
        String brand,
        String description,
        CategorySummary category,
        Boolean isActive
) {

    public static ProductDetail from (Product product, CategorySummary category) {
        return new ProductDetail(product.getId(),
                product.getName(), product.getBrand(),
                product.getDescription(),
                category, product.getIsActive());

    }
}
