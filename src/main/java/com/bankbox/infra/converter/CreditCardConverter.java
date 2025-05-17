package com.bankbox.infra.converter;

import com.bankbox.domain.entity.CreditCard;
import com.bankbox.infra.dto.CreditCardResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CreditCardConverter {
	CreditCardResponse toResponse(CreditCard creditCard);
	List<CreditCardResponse> toResponse(List<CreditCard> creditCards);
}
