package com.bankbox.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class BalanceDetailsResponse {
	@JsonProperty("user_id")
	public Long userId;
	@JsonProperty("total_balance")
	public BigDecimal totalBalance;
	@JsonProperty("checking_balance")
	public BigDecimal checkingBalance;
	@JsonProperty("savings_balance")
	public BigDecimal savingsBalance;
}
