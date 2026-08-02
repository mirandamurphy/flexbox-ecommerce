package com.flexbox.backend.marketing;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MarketingConsentActionConverter implements AttributeConverter<MarketingConsentAction, String> {

    @Override
    public String convertToDatabaseColumn(MarketingConsentAction attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public MarketingConsentAction convertToEntityAttribute(String dbData) {
        return dbData == null ? null : MarketingConsentAction.valueOf(dbData.toUpperCase());
    }
}
