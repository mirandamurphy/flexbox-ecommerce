package com.flexbox.backend.address;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// Converts Java Enums to lower case database values
@Converter(autoApply = true)
public class AddressTypeConverter implements AttributeConverter<AddressType, String> {

    @Override
    public String convertToDatabaseColumn(AddressType type) {
        return type == null ? null : type.name().toLowerCase();
    }

    @Override
    public AddressType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : AddressType.valueOf(dbData.toUpperCase());
    }
}
