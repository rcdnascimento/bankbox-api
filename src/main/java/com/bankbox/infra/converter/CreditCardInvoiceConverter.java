package com.bankbox.infra.converter;

import com.bankbox.domain.entity.CreditCardInvoice;
import com.bankbox.infra.dto.CreditCardInvoiceResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CreditCardInvoiceConverter {
	CreditCardInvoiceResponse toResponse(CreditCardInvoice invoice);
	List<CreditCardInvoiceResponse> toResponse(List<CreditCardInvoice> invoices);
}
