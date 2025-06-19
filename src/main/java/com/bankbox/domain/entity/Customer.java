package com.bankbox.domain.entity;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(unique = true)
	private String cpf;

	@Setter(AccessLevel.NONE)
	private String password;

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	private List<BankAccount> bankAccounts = new ArrayList<>();

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<CreditCard> creditCards = new ArrayList<>();

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<UnifiedCreditCard> unifiedCreditCards = new ArrayList<>();

	public BigDecimal getBalance() {
		return bankAccounts.stream()
			.map(BankAccount::getBalance)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public BigDecimal getBalanceFrom(BankAccountType type) {
		return bankAccounts.stream()
			.filter(account -> account.getType() == type)
			.map(BankAccount::getBalance)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public String getFirstName() {
		String space = " ";
		if (!name.contains(space)) return name;
		return name.substring(0, name.indexOf(space));
	}

	public void setPassword(String password) {
		this.password = new BCryptPasswordEncoder().encode(password);
	}

	public void addBankAccount(BankAccount bankAccount) {
		if (Objects.isNull(bankAccounts)) {
			bankAccounts = new ArrayList<>();
		}

		this.bankAccounts.add(bankAccount);
	}

	public void addCreditCard(CreditCard creditCard) {
		if (Objects.isNull(creditCards)) {
			creditCards = new ArrayList<>();
		}

		this.creditCards.add(creditCard);
	}

	public BigDecimal getTotalLimitFromAllCreditCards() {
		if (Objects.isNull(creditCards) || creditCards.isEmpty()) {
			return BigDecimal.ZERO;
		};

		return creditCards.stream()
			.filter(creditCard -> !creditCard.brand.equals("BANKBOX"))
			.map(CreditCard::getLimit)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
