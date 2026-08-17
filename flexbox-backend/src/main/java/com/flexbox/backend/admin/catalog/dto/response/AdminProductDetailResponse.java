package com.flexbox.backend.admin.catalog.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.product.model.Product;


import java.math.BigDecimal;

public record AdminProductDetailResponse(
        Long id,
        String sku,
        String brand,
        String name,
        String description,
        AdminCategoryResponse category,
        @JsonProperty("active") Boolean isActive,
        BigDecimal costPerUnit,
        AdminProductInventoryResponse inventory
        ){

        public static AdminProductDetailResponse from(Product product, AdminCategoryResponse category, AdminProductInventoryResponse inventory) {
                return new AdminProductDetailResponse(
                        product.getId(),
                        product.getSku(),
                        product.getBrand(),
                        product.getName(),
                        product.getDescription(),
                        category,
                        product.getIsActive(),
                        product.getCostPerUnit(),
                        inventory
                );
        }
}
