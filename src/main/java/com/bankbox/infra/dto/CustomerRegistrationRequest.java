package com.bankbox.infra.dto;

import javax.validation.constraints.NotBlank;

public class CustomerRegistrationRequest {
	public Long id;
	@NotBlank
	public String name;
	@NotBlank
	public String cpf;
	@NotBlank
	public String password;
}
