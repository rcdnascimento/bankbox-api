package com.bankbox.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public class DateTransactionsResponse {
	@JsonProperty("performed_at")
	public LocalDate performedAt;
	public List<TransactionResponse> transactions;

	public DateTransactionsResponse(LocalDate performedAt, List<TransactionResponse> transactions) {
		this.performedAt = performedAt;
		this.transactions = transactions;
	}
}
