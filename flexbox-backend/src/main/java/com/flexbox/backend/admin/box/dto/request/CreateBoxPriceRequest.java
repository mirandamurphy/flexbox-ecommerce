package com.flexbox.backend.admin.box.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CreateBoxPriceRequest(
        @NotNull
        @DecimalMin("0.01")BigDecimal amount,
        OffsetDateTime startsAt,
        OffsetDateTime endsAt
        ) {
}
