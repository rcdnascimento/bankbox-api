package com.bankbox.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class TransactionRequest {
	@NotBlank
	@JsonProperty("source_id")
	public Long sourceId;
	@NotBlank
	@JsonProperty("beneficiary_id")
	public Long beneficiaryId;
	@NotBlank
	public String type;
	@NotBlank
	public BigDecimal value;
}
