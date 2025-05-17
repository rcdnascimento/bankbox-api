package com.bankbox.domain;

import com.bankbox.domain.entity.BankAccount;
import com.bankbox.domain.entity.BankAccountType;
import com.bankbox.domain.entity.BankName;
import com.bankbox.domain.entity.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class CustomerTest {

	@Test
	void mustReturnTotalBalance() {
		Customer customer = new Customer();
		customer.addBankAccount(factoryBankAccount(customer, 733.22));
		customer.addBankAccount(factoryBankAccount(customer, 815.97));
		customer.addBankAccount(factoryBankAccount(customer, -18.99));

		Assertions.assertThat(customer.getBalance()).isEqualTo("1530.20");
	}

	private BankAccount factoryBankAccount(Customer customer, Double balance) {
		return new BankAccount(customer, BankName.ITAU, BankAccountType.SAVINGS, BigDecimal.valueOf(balance), "1322", "1233-2");
	}

}