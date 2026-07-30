package com.flexbox.backend.cart;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CartStatusConverter implements AttributeConverter<CartStatus, String> {

    @Override
    public String convertToDatabaseColumn(CartStatus attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public CartStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CartStatus.valueOf(dbData.toUpperCase());
    }
}
