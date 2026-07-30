package com.flexbox.backend.order;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CheckoutSessionModeConverter implements AttributeConverter<CheckoutSessionMode, String> {

    @Override
    public String convertToDatabaseColumn(CheckoutSessionMode attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public CheckoutSessionMode convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CheckoutSessionMode.valueOf(dbData.toUpperCase());
    }
}
