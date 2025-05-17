package com.bankbox.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BankResponse {
	public String name;
	@JsonProperty("formatted_name")
	public String formattedName;
	@JsonProperty("background_color")
	public String backgroundColor;
	@JsonProperty("img_url")
	public String imgUrl;
}
