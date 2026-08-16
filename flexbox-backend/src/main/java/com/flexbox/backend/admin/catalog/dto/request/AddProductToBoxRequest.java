package com.flexbox.backend.admin.catalog.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddProductToBoxRequest(
        @NotNull Long productId,
        @NotNull @Positive Integer quantity
) {
}
