package com.bankbox.infra.dto;

import com.bankbox.domain.entity.CategoryEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditCardTransactionResponse {
	public CategoryEnum category;

	public BigDecimal value;

	@JsonProperty(value = "credit_card_id")
	public Long creditCardId;

	@JsonProperty(value = "merchant_name")
	public String merchantName;

	public List<CreditCardInstallmentResponse> installments = new ArrayList<>();

	public CreditCardInvoiceResponse invoice;

	@JsonProperty("processed_at")
	public LocalDateTime processedAt;
}
