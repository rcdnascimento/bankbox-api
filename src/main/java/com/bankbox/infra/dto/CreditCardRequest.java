package com.bankbox.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreditCardRequest {
	@JsonProperty("owner_name")
	public String ownerName;
	public String number;
	public String expiration;
	@JsonProperty("security_number")
	public int securityNumber;
	public String type;
	public String brand;
	public Long limit;
}
