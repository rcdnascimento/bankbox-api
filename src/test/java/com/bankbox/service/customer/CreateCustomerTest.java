package com.bankbox.service.customer;

import com.bankbox.domain.entity.BankAccount;
import com.bankbox.domain.entity.BankName;
import com.bankbox.domain.entity.Customer;
import com.bankbox.domain.service.customer.CreateCustomer;
import com.bankbox.infra.repository.CustomerRepository;
import com.bankbox.domain.service.customer.impl.CustomerService;
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

@ContextConfiguration(classes = CustomerService.class)
@ExtendWith(SpringExtension.class)
class CreateCustomerTest {

	@Autowired
	private CreateCustomer createCustomer;

	@MockBean
	private CustomerRepository customerRepository;

	@BeforeEach
	public void setup() {
		Mockito.when(customerRepository.save(Mockito.any())).thenAnswer(it -> it.getArgument(0));
	}

	@Test
	void mustCreateCustomer() {
		Customer customer = new Customer();
		customer.setName("Richard");
		customer.setCpf("12345678901");

		Customer customerCreated = createCustomer.createCustomer(customer);

		Assertions.assertThat(customerCreated.getName()).isEqualTo(customer.getName());
		Assertions.assertThat(customerCreated.getCpf()).isEqualTo(customer.getCpf());
	}

	@Test
	void mustCreateCustomerWithTwoBankAccounts() {
		Customer customer = new Customer();
		customer.setName("Richard");
		customer.setCpf("12345678901");

		Customer customerCreated = createCustomer.createCustomer(customer);

		Assertions.assertThat(customerCreated.getBankAccounts()).hasSize(2);
	}

	@Test
	void mustCreateCustomerWithBankAccountsContainingValidNameAndBalances() {
		Customer customer = new Customer();
		customer.setName("Richard");
		customer.setCpf("12345678901");

		Customer customerCreated = createCustomer.createCustomer(customer);
		List<BankAccount> bankAccounts = customerCreated.getBankAccounts();

		Assertions.assertThat(bankAccounts.get(0).getBankName()).isEqualTo(BankName.ITAU);
		Assertions.assertThat(bankAccounts.get(0).getBalance()).isGreaterThan(new BigDecimal(1));
		Assertions.assertThat(bankAccounts.get(1).getBankName()).isEqualTo(BankName.NUBANK);
		Assertions.assertThat(bankAccounts.get(1).getBalance()).isGreaterThan(new BigDecimal(1));
	}
}