package com.flexbox.backend.admin.box.dto.response;


import com.flexbox.backend.catalog.box.model.SubscriptionBox;

public record BoxResponse(
        Long id,
        String name,
        String description,
        String imageFile,
        Integer availableUnits,
        Boolean isActive
) {

    public static BoxResponse from(SubscriptionBox box) {
        return new BoxResponse(
                box.getId(),
                box.getName(),
                box.getDescription(),
                box.getImageFile(),
                box.getAvailableUnits(),
                box.getIsActive()
        );
    }
}
