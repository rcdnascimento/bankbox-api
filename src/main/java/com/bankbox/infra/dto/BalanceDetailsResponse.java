package com.bankbox.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class BalanceDetailsResponse {
	@JsonProperty("customer_id")
	public Long customerId;
	@JsonProperty("total_balance")
	public BigDecimal totalBalance;
	@JsonProperty("checking_balance")
	public BigDecimal checkingBalance;
	@JsonProperty("savings_balance")
	public BigDecimal savingsBalance;
}
