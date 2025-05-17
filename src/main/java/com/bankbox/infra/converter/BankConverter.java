package com.bankbox.infra.converter;

import com.bankbox.domain.entity.BankName;
import com.bankbox.infra.dto.BankResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankConverter {
	@Mapping(expression = "java(bankName.name())", target = "name")
	BankResponse toResponse(BankName bankName);
	List<BankResponse> toResponse(List<BankName> banks);
}
