package com.flexbox.backend.admin.catalog.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.product.model.Product;

public record AdminProductSummaryResponse(Long id,
                                          String sku,
                                          String name,
                                          @JsonProperty("active") Boolean isActive

){

    public static AdminProductSummaryResponse from(Product product) {
        return new AdminProductSummaryResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getIsActive()
        );
    }
}
