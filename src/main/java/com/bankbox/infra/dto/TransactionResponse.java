package com.bankbox.infra.dto;

import com.bankbox.domain.entity.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse implements Cloneable {

	public Long id;
	@JsonProperty("source_id")
	public Long sourceId;
	@JsonProperty("beneficiary_id")
	public Long beneficiaryId;
	public BankAccountBasicResponse source;
	public BankAccountBasicResponse beneficiary;
	public TransactionType type;
	public BigDecimal value;
	@JsonProperty("performed_at")
	public LocalDateTime performedAt;
	public TransactionFlow flow;

	public TransactionResponse() { }

	public TransactionResponse(TransactionResponse transactionResponse) {
		this.id = transactionResponse.id;
		this.sourceId = transactionResponse.sourceId;
		this.beneficiaryId = transactionResponse.beneficiaryId;;
		this.source = transactionResponse.source;
		this.beneficiary = transactionResponse.beneficiary;
		this.type = transactionResponse.type;
		this.value = transactionResponse.value;
		this.performedAt = transactionResponse.performedAt;
		this.flow = transactionResponse.flow;
	}
}
