package com.bankbox.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class CreditCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String ownerName;
	@NotNull
	@ManyToOne
	private Customer customer;
	@NotNull
	private String number;
	@NotNull
	private String expiration;
	@NotNull
	private int securityNumber;
	@NotNull
	@Enumerated(EnumType.STRING)
	private CreditCardType type;
	@NotNull
	public String brand;
	@NotNull
	@Column(name = "`limit`")
	public BigDecimal limit;

	private static final int SECURITY_NUMBER_LENGTH = 3;
	private static final int CREDIT_CARD_LENGTH = 16;

	public void setNumber(String number) {
		String formattedNumber = number.replace(" ", "");
		if (formattedNumber.length() != CREDIT_CARD_LENGTH)
			throw new IllegalArgumentException("Incorrect credit card length");
		this.number = formattedNumber;
	}

	public String getLastNumbers() {
		return number.subSequence(12, 16).toString();
	}

	public void setSecurityNumber(int securityNumber) {
		if (String.valueOf(securityNumber).length() != SECURITY_NUMBER_LENGTH)
			throw new IllegalArgumentException("Incorrect credit card security number");
		this.securityNumber = securityNumber;
	}
}
