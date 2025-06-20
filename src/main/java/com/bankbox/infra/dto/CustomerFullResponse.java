package com.bankbox.infra.dto;

import java.util.List;

public class CustomerFullResponse {
	public Long id;
	public String name;
	public String firstName;
	public String cpf;
	public List<CreditCardResponse> creditCards;
}
