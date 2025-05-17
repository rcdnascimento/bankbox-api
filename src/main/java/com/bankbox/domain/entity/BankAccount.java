package com.bankbox.domain.entity;

import com.bankbox.domain.exception.BalanceNotEnoughException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BankAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	@ManyToOne
	private Customer owner;
	@NotNull
	@Enumerated(EnumType.STRING)
	private BankName bankName;
	@NotNull
	@Enumerated(EnumType.STRING)
	private BankAccountType type;
	@NotNull
	private String agency;
	@NotNull
	private String account;
	@NotNull
	private BigDecimal balance;
	@Column(unique = true)
	private String pixKey;

	public BankAccount(Customer owner, BankName bankName, BankAccountType type, BigDecimal balance, String agency, String account) {
		this.owner = owner;
		this.bankName = bankName;
		this.type = type;
		this.balance = balance;
		this.agency = agency;
		this.account = account;
	}

	public void transfer(BankAccount beneficiary, BigDecimal value) {
		withdraw(value);
		beneficiary.deposit(value);
	}

	public void withdraw(BigDecimal value) {
		if (value.compareTo(balance) > 0) {
			throw new BalanceNotEnoughException();
		}

		balance = balance.subtract(value);
	}

	public void deposit(BigDecimal value) {
		balance = balance.add(value);
	}
}
