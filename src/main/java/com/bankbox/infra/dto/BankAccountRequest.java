package com.bankbox.infra.dto;

import com.bankbox.domain.entity.BankName;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BankAccountRequest {
	@JsonProperty("customer_id")
	public Long customerId;
	@JsonProperty("bank_id")
	public Long bankId;
	public ConsentRequest consent;
}
