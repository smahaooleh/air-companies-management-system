package com.osmaha.aircompaniesmanagementsystem.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import java.time.Duration;

@Convert
public class DurationToStringConverter implements AttributeConverter<Duration, String> {

    @Override
    public String convertToDatabaseColumn(Duration attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public Duration convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Duration.parse(dbData);
    }
}
