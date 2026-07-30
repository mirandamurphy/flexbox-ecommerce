package com.flexbox.backend.order;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CheckoutSessionStatusConverter implements AttributeConverter<CheckoutSessionStatus, String> {

    @Override
    public String convertToDatabaseColumn(CheckoutSessionStatus attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public CheckoutSessionStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CheckoutSessionStatus.valueOf(dbData.toUpperCase());
    }
}
