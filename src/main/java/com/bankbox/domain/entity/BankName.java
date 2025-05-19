package com.bankbox.domain.entity;

import lombok.Getter;

@Getter
public enum BankName {
	BRADESCO("Bradesco", "#cc092f", "../../assets/imgs/banks/bradesco.png"),
	BANCO_DO_BRASIL("Banco do Brasil", "", "../../assets/imgs/banks/banco_do_brasil.png"),
	ITAU("Itaú Unibanco", "#EF761C", "../../assets/imgs/banks/itau.png"),
	SANTANDER("Santander", "#fe0000", "../../assets/imgs/banks/santander.png"),
	NUBANK("Nubank", "#781BC9", "../../assets/imgs/banks/nubank.png");

	private final String formattedName;
	private final String backgroundColor;
	private final String imgUrl;

	BankName(String formattedName, String backgroundColor, String imgUrl) {
		this.formattedName = formattedName;
		this.backgroundColor = backgroundColor;
		this.imgUrl = imgUrl;
	}
}
