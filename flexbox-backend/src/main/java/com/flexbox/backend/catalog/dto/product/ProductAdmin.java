package com.flexbox.backend.catalog.dto.product;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProductAdmin(
        Long id,
        String sku,
        String brand,
        String description,
        BigDecimal costPerUnit,
        Boolean isActive,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
