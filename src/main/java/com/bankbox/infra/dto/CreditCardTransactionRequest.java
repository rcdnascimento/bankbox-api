package com.bankbox.infra.dto;

import com.bankbox.domain.entity.CategoryEnum;
import com.bankbox.domain.entity.CreditCard;
import com.bankbox.domain.entity.CreditCardInstallment;
import com.bankbox.domain.entity.CreditCardInvoice;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CreditCardTransactionRequest {
	@NotNull
	@JsonProperty(required = true)
	public CategoryEnum category;

	@NotNull
	@JsonProperty(required = true)
	public BigDecimal value;

	public Long creditCardId;

	@NotNull
	@JsonProperty(value = "merchant_name", required = true)
	public String merchantName;

	@NotNull
	@JsonProperty(required = true)
	public Integer installments;

	@NotNull
	@JsonProperty(value = "processed_at", required = true)
	public LocalDateTime processedAt;
}
