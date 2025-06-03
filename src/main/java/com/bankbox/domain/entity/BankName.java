package com.bankbox.domain.entity;

import lombok.Getter;

@Getter
public enum BankName {
	BRADESCO(1, "Bradesco", "#cc092f", "../../assets/imgs/banks/bradesco.png"),
	BANCO_DO_BRASIL(2, "Banco do Brasil", "", "../../assets/imgs/banks/banco_do_brasil.png"),
	ITAU(3, "Itaú Unibanco", "#EF761C", "../../assets/imgs/banks/itau.png"),
	SANTANDER(4, "Santander", "#fe0000", "../../assets/imgs/banks/santander.png"),
	NUBANK(5, "Nubank", "#781BC9", "../../assets/imgs/banks/nubank.png");

	private final Integer id;
	private final String formattedName;
	private final String backgroundColor;
	private final String imgUrl;

	BankName(Integer id, String formattedName, String backgroundColor, String imgUrl) {
		this.id = id;
		this.formattedName = formattedName;
		this.backgroundColor = backgroundColor;
		this.imgUrl = imgUrl;
	}
}
