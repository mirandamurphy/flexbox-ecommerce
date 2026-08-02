package com.flexbox.backend.subscription;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SubscriptionPlanStatusConverter implements AttributeConverter<SubscriptionPlanStatus, String> {

    @Override
    public String convertToDatabaseColumn(SubscriptionPlanStatus attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public SubscriptionPlanStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : SubscriptionPlanStatus.valueOf(dbData.toUpperCase());
    }
}
