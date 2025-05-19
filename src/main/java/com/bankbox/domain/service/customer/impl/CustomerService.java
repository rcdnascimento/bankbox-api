package com.bankbox.domain.service.customer.impl;

import com.bankbox.domain.entity.*;
import com.bankbox.domain.exception.CustomerAlreadyExistsException;
import com.bankbox.domain.exception.CustomerNotFoundException;
import com.bankbox.domain.service.bankaccount.impl.BankAccountService;
import com.bankbox.domain.service.creditcard.impl.CreditCardService;
import com.bankbox.infra.repository.CustomerRepository;
import com.bankbox.domain.service.customer.CreateCustomer;
import com.bankbox.domain.service.customer.RetrieveCustomer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CustomerService implements RetrieveCustomer, CreateCustomer {

	private final CustomerRepository customerRepository;
	private final BankAccountService bankAccountService;
	private final CreditCardService creditCardService;

	private static final String DEFAULT_EXPIRATION = "2031-06";

	@Override
	public List<Customer> retrieveAll() {
		return customerRepository.findAllCustomers();
	}

	@Override
	public Customer retrieveById(Long id) {
		return customerRepository.findCustomerById(id).orElseThrow(CustomerNotFoundException::new);
	}

	@Override
	public Customer retrieveByCpf(String cpf) {
		return customerRepository.findCustomerByCpf(cpf).orElseThrow(CustomerNotFoundException::new);
	}

	@Override
	public boolean existsById(Long id) {
		try {
			return customerRepository.customerExistsById(id) == 1;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	@Transactional
	public Customer createCustomer(Customer customer) {
		Optional<Customer> existentCustomer = customerRepository.findCustomerByCpf(customer.getCpf());
		if (existentCustomer.isPresent()) {
			throw new CustomerAlreadyExistsException();
		}

		Customer generatedCustomer = addRandomData(customer);
		customerRepository.insertCustomer(generatedCustomer.getName(), generatedCustomer.getCpf(), generatedCustomer.getPassword());
		Customer createdCustomer = customerRepository.retrieveLastCreated();

		for (BankAccount bankAccount : generatedCustomer.getBankAccounts()) {
			bankAccount.setOwner(createdCustomer);
			bankAccountService.addBankAccount(bankAccount);
		}

		for (CreditCard creditCard : generatedCustomer.getCreditCards()) {
			creditCard.setCustomer(createdCustomer);
			creditCardService.addCreditCard(creditCard);
		}

		return createdCustomer;
	}

	private Customer addRandomData(Customer customer) {
		Customer withBanks = addRandomBankAccounts(customer);
		return addRandomCreditCards(withBanks);
	}

	private Customer addRandomBankAccounts(Customer customer) {
		BankAccount itau = generateFakeBankAccount(customer, BankName.ITAU, BankAccountType.CHECKING);
		BankAccount nubank = generateFakeBankAccount(customer, BankName.NUBANK, BankAccountType.SAVINGS);
		customer.addBankAccount(itau);
		customer.addBankAccount(nubank);
		return customer;
	}

	private Customer addRandomCreditCards(Customer customer) {
		CreditCard visa = generateFakeCreditCard(customer, CreditCardType.VIRTUAL, "VISA");
		CreditCard mastercard = generateFakeCreditCard(customer, CreditCardType.PHYSICAL, "MASTERCARD");
		customer.addCreditCard(visa);
		customer.addCreditCard(mastercard);
		return customer;
	}

	private BankAccount generateFakeBankAccount(Customer owner, BankName bankName, BankAccountType type) {
		return new BankAccount(owner, bankName, type, generateRandomBalance(), generateRandomAgency(), generateRandomNumber());
	}

	private CreditCard generateFakeCreditCard(Customer owner, CreditCardType type, String brand) {
		return CreditCard.builder()
			.ownerName(owner.getName())
			.number(generateCardNumber())
			.securityNumber(generateSecurityNumber())
			.brand(brand)
			.expiration(DEFAULT_EXPIRATION)
			.type(type)
			.limit(BigDecimal.valueOf(500 + new Random().nextInt(1000)))
			.build();
	}

	private Integer generateSecurityNumber() {
		return 100 + new Random().nextInt(899);
	}

	private String generateCardNumber() {
		String number = "";
		for (int i = 0; i < 4; i++) {
			number = number.concat(String.valueOf(1000 + new Random().nextInt(8999)));
		}
		return number;
	}

	private BigDecimal generateRandomBalance() {
		return new BigDecimal(BigInteger.valueOf(new Random().nextInt(100001)), 2);
	}

	private String generateRandomAgency() {
		return String.valueOf(1000 + new Random().nextInt(8999));
	}

	private String generateRandomNumber() {
		return 10000 + new Random().nextInt(89999) + "-" + new Random().nextInt(9);
	}
}
