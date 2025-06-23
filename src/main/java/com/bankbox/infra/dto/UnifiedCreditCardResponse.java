package com.bankbox.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnifiedCreditCardResponse {
	public Long id;
	public String number;
	public String expiration;
	@JsonProperty("last_numbers")
	public String lastNumbers;
	@JsonProperty("security_number")
	public int securityNumber;
	public String type = "CREDIT_CARD";
	public Long limit;
}
