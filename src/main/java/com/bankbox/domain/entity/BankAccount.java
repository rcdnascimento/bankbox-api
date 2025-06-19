package com.bankbox.domain.entity;

import com.bankbox.domain.exception.BalanceNotEnoughException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BankAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Customer owner;

	@ManyToOne
	@JoinColumn(name = "bank_id")
	private Bank bank;

	@Enumerated(EnumType.STRING)
	private BankAccountType type;

	@NotNull
	private String agency;

	@NotNull
	private String account;

	@NotNull
	private BigDecimal balance;

	@Column(unique = true)
	@OneToMany(mappedBy = "bankAccount")
	private List<PixKey> pixKeys = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "consent_id")
	private Consent consent;

	public BankAccount(Long id) {
		this.id = id;
	}

	public BankAccount(Customer owner, Bank bank, BankAccountType type, BigDecimal balance, String agency, String account, Consent consent) {
		this.owner = owner;
		this.bank = bank;
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
