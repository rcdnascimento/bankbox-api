package com.bankbox.converter;

import com.bankbox.domain.entity.BankAccount;
import com.bankbox.domain.entity.BankAccountType;
import com.bankbox.domain.entity.Customer;
import com.bankbox.domain.entity.Transaction;
import com.bankbox.domain.entity.TransactionType;
import com.bankbox.infra.converter.BankAccountConverterImpl;
import com.bankbox.infra.converter.BankConverterImpl;
import com.bankbox.infra.converter.TransactionConverter;
import com.bankbox.infra.dto.DateTransactionsResponse;
import com.bankbox.infra.dto.TransactionFlow;
import com.bankbox.infra.dto.TransactionResponse;
import com.bankbox.domain.service.bankaccount.impl.BankAccountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ContextConfiguration(
	classes = {
		TransactionConverter.class,
		BankAccountConverterImpl.class,
		BankConverterImpl.class
	}
)
@ExtendWith(SpringExtension.class)
class TransactionConverterTest {

	@Autowired
	private TransactionConverter transactionConverter;

	@MockBean
	private BankAccountService bankAccountService;

	@Test
	void shouldConvertTransactionToDateTransaction() {
		BankAccount source = factoryBankAccount(1L, "Richard Nascimento");
		BankAccount beneficiary = factoryBankAccount(2L, "El Santo");
		Transaction transaction = new Transaction(source, beneficiary, TransactionType.TRANSFERENCE, BigDecimal.ONE);
		List<DateTransactionsResponse> dateTransactionsResponse = transactionConverter.toDateTransactionResponse(List.of(transaction), 1L);
		List<TransactionResponse> transactions = dateTransactionsResponse.get(0).transactions;

		Assertions.assertThat(dateTransactionsResponse).hasSize(1);
		Assertions.assertThat(transactions).hasSize(1);
		Assertions.assertThat(dateTransactionsResponse.get(0).performedAt).isEqualTo(LocalDate.now());
		Assertions.assertThat(transactions.get(0).source.customerFirstName).isEqualTo("Richard");
		Assertions.assertThat(transactions.get(0).beneficiary.customerFirstName).isEqualTo("El");
	}

	@Test
	void shouldSeparateTransactionsByDataWhileConvertingToDateTransaction() {
		BankAccount source = factoryBankAccount(1L, "Richard Nascimento");
		BankAccount beneficiary = factoryBankAccount(2L,"El Santo");
		Transaction transaction = new Transaction(source, beneficiary, TransactionType.TRANSFERENCE, BigDecimal.ONE);
		Transaction transactionTwo = new Transaction(source, beneficiary, TransactionType.TRANSFERENCE, BigDecimal.ONE);
		List<DateTransactionsResponse> dateTransactionsResponse = transactionConverter.toDateTransactionResponse(List.of(transaction, transactionTwo), 1L);
		List<TransactionResponse> transactions = dateTransactionsResponse.get(0).transactions;

		Assertions.assertThat(dateTransactionsResponse).hasSize(1);
		Assertions.assertThat(transactions).hasSize(2);
	}

	@Test
	void shouldConvertTransactionFlowWhenSourceAndBeneficiaryAreTheSame() {
		BankAccount source = factoryBankAccount(1L, "Richard Nascimento");
		BankAccount beneficiary = factoryBankAccount(1L, "Richard Nascimento");
		Transaction transaction = new Transaction(source, beneficiary, TransactionType.TRANSFERENCE, BigDecimal.ONE);
		List<DateTransactionsResponse> dateTransactionsResponse = transactionConverter.toDateTransactionResponse(List.of(transaction), 1L);
		List<TransactionResponse> transactions = dateTransactionsResponse.get(0).transactions;

		Assertions.assertThat(dateTransactionsResponse).hasSize(1);
		Assertions.assertThat(transactions).hasSize(2);
		Assertions.assertThat(transactions.get(0).flow).isEqualTo(TransactionFlow.OUTBOUND);
		Assertions.assertThat(transactions.get(1).flow).isEqualTo(TransactionFlow.INBOUND);
	}

	private BankAccount factoryBankAccount(Long customerId, String name) {
		Customer customer = new Customer();
		customer.setId(customerId);
		customer.setName(name);
		BankAccount bankAccount = new BankAccount();
		bankAccount.setType(BankAccountType.CHECKING);
		bankAccount.setOwner(customer);
		bankAccount.setBalance(BigDecimal.valueOf(1000));
		return bankAccount;
	}
}
