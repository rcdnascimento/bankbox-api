package com.bankbox.domain.entity;

import lombok.Getter;

@Getter
public enum RandomCodeType {
	ALPHANUMERIC("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"),
	NUMERIC("0123456789");

	private final String characters;

	RandomCodeType(String characters) {
		this.characters = characters;
	}

}
