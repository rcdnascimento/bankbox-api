package com.bankbox.domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String ownerName;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
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

	@OneToMany(mappedBy = "creditCard", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<CreditCardInvoice> invoices = new ArrayList<>();

	@OneToMany(mappedBy = "creditCard", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<CreditCardTransaction> transactions = new ArrayList<>();

	private static final int SECURITY_NUMBER_LENGTH = 3;
	private static final int CREDIT_CARD_LENGTH = 16;

	public CreditCard(Long id) {
		this.id = id;
	}

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
