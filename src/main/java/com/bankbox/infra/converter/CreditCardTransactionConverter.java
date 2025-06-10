package com.bankbox.infra.converter;

import com.bankbox.domain.entity.CreditCardTransaction;
import com.bankbox.infra.dto.CreditCardTransactionResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CreditCardTransactionConverter {
  CreditCardTransactionResponse toResponse(CreditCardTransaction transaction);
  List<CreditCardTransactionResponse> toResponse(List<CreditCardTransaction> transactions);
}
