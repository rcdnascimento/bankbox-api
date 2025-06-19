package com.bankbox.infra.converter;

import com.bankbox.domain.entity.CreditCard;
import com.bankbox.domain.entity.UnifiedCreditCard;
import com.bankbox.infra.dto.CreditCardRequest;
import com.bankbox.infra.dto.CreditCardResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CreditCardConverter {
	CreditCardResponse toResponse(CreditCard creditCard);
	List<CreditCardResponse> toResponse(List<CreditCard> creditCards);

	@Mapping(source = "creditCard.ownerName", target = "ownerName")
	@Mapping(source = "creditCard.number", target = "number")
	@Mapping(source = "creditCard.expiration", target = "expiration")
	@Mapping(source = "creditCard.securityNumber", target = "securityNumber")
	@Mapping(source = "creditCard.type", target = "type")
	@Mapping(source = "creditCard.brand", target = "brand")
	@Mapping(source = "creditCard.limit", target = "limit")
	@Mapping(source = "customerId", target = "customer.id")
	CreditCard toEntity(CreditCardRequest creditCard, Long customerId);

	CreditCardResponse toResponse(UnifiedCreditCard unifiedCreditCard);
}
