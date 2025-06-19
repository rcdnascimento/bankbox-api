package com.bankbox.infra.dto;

import com.bankbox.domain.entity.BankName;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ConsentRequest {
	public String code;
	public List<Long> roles;
}
