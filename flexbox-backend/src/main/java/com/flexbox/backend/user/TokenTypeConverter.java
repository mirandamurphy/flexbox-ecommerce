package com.flexbox.backend.user;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TokenTypeConverter implements AttributeConverter<TokenType, String> {

    @Override
    public String convertToDatabaseColumn(TokenType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public TokenType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TokenType.valueOf(dbData.toUpperCase());
    }
}
