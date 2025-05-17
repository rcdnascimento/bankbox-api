package com.bankbox.infra.dto;

import com.bankbox.domain.entity.BankAccountType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BankAccountBasicResponse {
	public Long id;
	@JsonProperty("customer_first_name")
	public String customerFirstName;
	public BankResponse bank;
	public BankAccountType type;
	public String agency;
	public String account;
}
