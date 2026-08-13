package com.flexbox.backend.catalog.product.response;

import com.flexbox.backend.catalog.product.model.Category;

public record CategorySummaryResponse(
        Long id,
        String name
) {
    public static CategorySummaryResponse from(Category category) {
        return new CategorySummaryResponse(category.getId(),
                category.getName());
    }
}
