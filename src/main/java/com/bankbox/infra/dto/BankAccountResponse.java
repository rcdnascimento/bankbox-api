package com.bankbox.infra.dto;

import com.bankbox.domain.entity.BankAccountType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankAccountResponse {
	public Long id;
	@JsonProperty("customer_id")
	public Long customerId;
	public BankResponse bank;
	public BankAccountType type;
	public String agency;
	public String account;
	@JsonProperty("pix_key")
	public String pixKey;
	public BigDecimal balance;
}
