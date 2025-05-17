package com.bankbox.service.transaction;

import com.bankbox.domain.entity.*;
import com.bankbox.domain.entity.Customer;
import com.bankbox.domain.service.transaction.ExecuteTransaction;
import com.bankbox.infra.repository.TransactionRepository;
import com.bankbox.domain.service.customer.RetrieveCustomer;
import com.bankbox.domain.service.transaction.v1.TransactionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

@ContextConfiguration(classes = TransactionService.class)
@ExtendWith(SpringExtension.class)
class ExecuteTransactionTest {

	@Autowired
	private ExecuteTransaction executeTransaction;

	@MockBean
	private TransactionRepository transactionRepository;

	@MockBean
	private RetrieveCustomer retrieveCustomer;

	@BeforeEach
	public void setup() {
		Mockito.when(transactionRepository.saveAll(Mockito.any())).then(it -> it.getArgument(0));
	}

	@Test
	void mustTransferMoneyBetweenCustomersWhenThereAreOneSourceAccount() {
		Customer source = new Customer();
		BankAccount sourceAccount = factoryBankAccount(source, 845.18);
		source.addBankAccount(sourceAccount);
		Customer beneficiary = new Customer();
		BankAccount beneficiaryAccount = factoryBankAccount(beneficiary, 99.23);
		beneficiary.addBankAccount(beneficiaryAccount);

		Transaction transaction = new Transaction(sourceAccount, beneficiaryAccount, TransactionType.PIX, BigDecimal.valueOf(242.13));

		executeTransaction.executeTransactions(List.of(transaction));

		Assertions.assertThat(source.getBalance()).isEqualTo("603.05");
		Assertions.assertThat(beneficiary.getBalance()).isEqualTo("341.36");
	}

	@Test
	void mustTransferMoneyBetweenCustomersWhenThereAreTwoSourceAccounts() {
		Customer source = new Customer();
		BankAccount sourceAccount = factoryBankAccount(source, 845.18);
		source.addBankAccount(sourceAccount);
		Customer beneficiary = new Customer();
		BankAccount beneficiaryAccount = factoryBankAccount(beneficiary, 99.23);
		beneficiary.addBankAccount(beneficiaryAccount);

		Transaction transferenceOne = new Transaction(sourceAccount, beneficiaryAccount, TransactionType.PIX, BigDecimal.valueOf(242.13));
		Transaction transferenceTwo = new Transaction(sourceAccount, beneficiaryAccount, TransactionType.PIX, BigDecimal.valueOf(511.11));
		Transaction transferenceThree = new Transaction(sourceAccount, beneficiaryAccount, TransactionType.PIX, BigDecimal.valueOf(5.12));

		executeTransaction.executeTransactions(List.of(transferenceOne, transferenceTwo, transferenceThree));

		Assertions.assertThat(source.getBalance()).isEqualTo("86.82");
		Assertions.assertThat(beneficiary.getBalance()).isEqualTo("857.59");
	}

	private BankAccount factoryBankAccount(Customer customer, Double balance) {
		return new BankAccount(customer, BankName.ITAU, BankAccountType.SAVINGS, BigDecimal.valueOf(balance), "1322", "1233-2");
	}

}
