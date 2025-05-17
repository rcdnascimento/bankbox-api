package com.bankbox.infra.repository.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.YearMonth;

@Converter
public class YearMonthJpaConverter implements AttributeConverter<YearMonth, String> {
	@Override
	public String convertToDatabaseColumn(YearMonth attribute) {
		return String.valueOf(attribute);
	}

	@Override
	public YearMonth convertToEntityAttribute(String dbData) {
		return YearMonth.parse(dbData);
	}
}
