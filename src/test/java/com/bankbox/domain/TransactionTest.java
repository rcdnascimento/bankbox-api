package com.bankbox.domain;

import com.bankbox.domain.entity.*;
import com.bankbox.infra.dto.TransactionFlow;
import com.bankbox.domain.exception.BalanceNotEnoughException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

	@Test
	void mustTransferMoneyBetweenCustomersWhenThereAreOneSourceAccount() {
		Customer source = new Customer();
		BankAccount sourceAccount = factoryBankAccount(new Customer(), 845.18);
		source.addBankAccount(sourceAccount);
		Customer beneficiary = new Customer();
		BankAccount beneficiaryAccount = factoryBankAccount(beneficiary, 99.23);
		beneficiary.addBankAccount(beneficiaryAccount);

		Transaction transaction = new Transaction(sourceAccount, beneficiaryAccount, TransactionType.PIX, BigDecimal.valueOf(242.13));

		transaction.execute();

		Assertions.assertThat(source.getBalance()).isEqualTo("603.05");
		Assertions.assertThat(beneficiary.getBalance()).isEqualTo("341.36");
	}

	@Test
	void mustThrowExceptionWhenSourceDoesNotHaveEnoughBalance() {
		Customer source = new Customer();
		BankAccount sourceAccount = factoryBankAccount(new Customer(), 845.18);
		source.addBankAccount(sourceAccount);
		Customer beneficiary = new Customer();
		BankAccount beneficiaryAccount = factoryBankAccount(beneficiary, 99.23);
		beneficiary.addBankAccount(beneficiaryAccount);

		Exception exception = assertThrows(BalanceNotEnoughException.class, () -> new Transaction(sourceAccount, beneficiaryAccount, TransactionType.PIX, BigDecimal.valueOf(845.19)));

		Assertions.assertThat(exception.getMessage()).isEqualTo("Source balance is not enough");
	}

	@Test
	void mustReturnInboundFlowWhenCustomerIsBeneficiary() {
		Customer source = new Customer();
		source.setId(1L);
		BankAccount sourceAccount = factoryBankAccount(source, 845.18);
		source.addBankAccount(sourceAccount);
		Customer beneficiary = new Customer();
		beneficiary.setId(2L);
		BankAccount beneficiaryAccount = factoryBankAccount(beneficiary, 99.23);
		beneficiary.addBankAccount(beneficiaryAccount);

		Transaction transaction = new Transaction(sourceAccount, beneficiaryAccount, TransactionType.PIX, BigDecimal.valueOf(242.13));

		Assertions.assertThat(transaction.extractFlowForCustomer(2L)).isEqualTo(TransactionFlow.INBOUND);
	}

	@Test
	void mustReturnOutboundFlowWhenCustomerIsSource() {
		Customer source = new Customer();
		source.setId(1L);
		BankAccount sourceAccount = factoryBankAccount(source, 845.18);
		source.addBankAccount(sourceAccount);
		Customer beneficiary = new Customer();
		beneficiary.setId(2L);
		BankAccount beneficiaryAccount = factoryBankAccount(beneficiary, 99.23);
		beneficiary.addBankAccount(beneficiaryAccount);

		Transaction transaction = new Transaction(sourceAccount, beneficiaryAccount, TransactionType.PIX, BigDecimal.valueOf(242.13));

		Assertions.assertThat(transaction.extractFlowForCustomer(1L)).isEqualTo(TransactionFlow.OUTBOUND);
	}

	@Test
	void mustThrowExceptionWhenCustomerIsNotInvoledInTransaction() {
		Customer source = new Customer();
		source.setId(1L);
		BankAccount sourceAccount = factoryBankAccount(source, 845.18);
		source.addBankAccount(sourceAccount);
		Customer beneficiary = new Customer();
		beneficiary.setId(2L);
		BankAccount beneficiaryAccount = factoryBankAccount(beneficiary, 99.23);
		beneficiary.addBankAccount(beneficiaryAccount);

		Transaction transaction = new Transaction(sourceAccount, beneficiaryAccount, TransactionType.PIX, BigDecimal.valueOf(242.13));

		assertThrows(IllegalArgumentException.class, () -> transaction.extractFlowForCustomer(3L));
}

	private BankAccount factoryBankAccount(Customer customer, Double balance) {
		return new BankAccount(customer, BankName.ITAU, BankAccountType.SAVINGS, BigDecimal.valueOf(balance), "1322", "1233-2");
	}
}