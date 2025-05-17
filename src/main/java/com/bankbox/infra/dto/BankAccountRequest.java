package com.bankbox.infra.dto;

import com.bankbox.domain.entity.BankName;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BankAccountRequest {
	@JsonProperty("customer_id")
	public Long customerId;
	@JsonProperty("bank_name")
	public BankName bankName;
}
