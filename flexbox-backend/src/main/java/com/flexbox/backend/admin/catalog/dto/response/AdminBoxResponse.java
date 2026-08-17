package com.flexbox.backend.admin.catalog.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexbox.backend.catalog.box.model.SubscriptionBox;

public record AdminBoxResponse(
        Long id,
        String name,
        String description,
        @JsonProperty("imagePath") String imageFile,
        Integer availableUnits,
        Boolean isActive
) {

    public static AdminBoxResponse from(SubscriptionBox box) {
        return new AdminBoxResponse(
                box.getId(),
                box.getName(),
                box.getDescription(),
                box.getImageFile(),
                box.getAvailableUnits(),
                box.getIsActive()
        );
    }
}
