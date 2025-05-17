package com.bankbox.converter;

import com.bankbox.domain.entity.BankAccount;
import com.bankbox.domain.entity.BankAccountType;
import com.bankbox.domain.entity.BankName;
import com.bankbox.domain.entity.Customer;
import com.bankbox.infra.converter.CustomerConverter;
import com.bankbox.infra.converter.CustomerConverterImpl;
import com.bankbox.infra.dto.BalanceDetailsResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@ContextConfiguration(classes = CustomerConverterImpl.class)
@ExtendWith(SpringExtension.class)
class CustomerConverterTest {

	@Autowired
	private CustomerConverter customerConverter;

	@Test
	void totalBalanceMustBeCorrect() {
		Customer customer = new Customer();
		customer.addBankAccount(factoryBankAccount(customer, BankAccountType.SAVINGS, 1000.0));
		customer.addBankAccount(factoryBankAccount(customer, BankAccountType.CHECKING, 2000.0));

		BalanceDetailsResponse balanceDetails = customerConverter.toBalanceDetails(customer);

		Assertions.assertThat(balanceDetails.totalBalance).isEqualTo("3000.0");
	}

	@Test
	void checkingBalanceMustBeCorrect() {
		Customer customer = new Customer();
		customer.addBankAccount(factoryBankAccount(customer, BankAccountType.SAVINGS, 1000.0));
		customer.addBankAccount(factoryBankAccount(customer, BankAccountType.CHECKING, 2000.0));

		BalanceDetailsResponse balanceDetails = customerConverter.toBalanceDetails(customer);

		Assertions.assertThat(balanceDetails.checkingBalance).isEqualTo("2000.0");
	}

	@Test
	void savingsBalanceMustBeCorrect() {
		Customer customer = new Customer();
		customer.addBankAccount(factoryBankAccount(customer, BankAccountType.SAVINGS, 1000.0));
		customer.addBankAccount(factoryBankAccount(customer, BankAccountType.CHECKING, 2000.0));

		BalanceDetailsResponse balanceDetails = customerConverter.toBalanceDetails(customer);

		Assertions.assertThat(balanceDetails.savingsBalance).isEqualTo("1000.0");
	}

	private BankAccount factoryBankAccount(Customer customer, BankAccountType type, Double balance) {
		return new BankAccount(customer, BankName.ITAU, type, BigDecimal.valueOf(balance), "1322", "1233-2");
	}
}