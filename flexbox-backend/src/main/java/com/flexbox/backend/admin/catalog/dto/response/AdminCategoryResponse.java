package com.flexbox.backend.admin.catalog.dto.response;

import com.flexbox.backend.catalog.product.model.Category;

public record AdminCategoryResponse(
        Long id,
        String name

) {
    public static AdminCategoryResponse from(Category category) {
        return new AdminCategoryResponse(
                category.getId(),
                category.getName()
        );
    }
}
