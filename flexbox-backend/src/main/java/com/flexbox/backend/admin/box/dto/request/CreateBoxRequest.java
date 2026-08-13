package com.flexbox.backend.admin.box.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBoxRequest(
        @NotBlank String name,
        String description,
        String imagePath,
        @NotNull Integer availableUnits,
        Boolean isActive
) {

}
