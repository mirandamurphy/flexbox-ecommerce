package com.flexbox.backend.admin.box.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.product.model.Product;


import java.math.BigDecimal;

public record ProductDetailResponse(
        Long id,
        String sku,
        String brand,
        String name,
        String description,
        CategoryResponse category,
        @JsonProperty("active") Boolean isActive,
        BigDecimal costPerUnit,
        ProductInventoryResponse inventory
        ){

        public static ProductDetailResponse from(Product product, CategoryResponse category, ProductInventoryResponse inventory) {
                return new ProductDetailResponse(
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
