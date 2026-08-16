package com.flexbox.backend.admin.catalog.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CreateBoxPriceRequest(
        @NotNull
        @JsonProperty("price") @DecimalMin("0.01")BigDecimal amount,
        OffsetDateTime startsAt,
        OffsetDateTime endsAt
        ) {
}
