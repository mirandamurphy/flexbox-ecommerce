package com.flexbox.backend.admin.box.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddProductToBoxRequest(
        @NotNull Long productId,
        @NotNull @Positive Integer quantity
) {
}
