package com.flexbox.backend.catalog.dto.category;

import com.flexbox.backend.catalog.entity.Category;

public record CategorySummary(
        Long id,
        String name
) {
    public static CategorySummary from(Category category) {
        return new CategorySummary(category.getId(),
                category.getName());
    }
}
