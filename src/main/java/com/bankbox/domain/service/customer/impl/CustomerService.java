package com.bankbox.domain.service.customer.impl;

import com.bankbox.domain.entity.*;
import com.bankbox.domain.exception.CustomerAlreadyExistsException;
import com.bankbox.domain.exception.CustomerNotFoundException;
import com.bankbox.domain.exception.InvalidRegistrationCode;
import com.bankbox.domain.service.bankaccount.impl.BankAccountService;
import com.bankbox.domain.service.creditcard.impl.CreditCardService;
import com.bankbox.infra.repository.CustomerRegistrationRepository;
import com.bankbox.infra.repository.CustomerRepository;
import com.bankbox.domain.service.customer.CreateCustomer;
import com.bankbox.domain.service.customer.RetrieveCustomer;
import com.bankbox.infra.repository.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService implements RetrieveCustomer, CreateCustomer {

	private final CustomerRepository customerRepository;
	private final BankAccountService bankAccountService;
	private final CreditCardService creditCardService;
	private final CustomerRegistrationRepository customerRegistrationRepository;

	private static final String DEFAULT_EXPIRATION = "2031-06";
	private final PixKeyRepository pixKeyRepository;

	@Override
	public long countCustomers() {
		return customerRepository.count();
	}

	@Override
	public List<Customer> retrieveAll() {
		return customerRepository.findAllCustomers();
	}

	@Override
	public Long retrieveLastCustomerId() {
		return customerRepository.count();
	}

	@Override
	public Page<Customer> retrievePaginated(int page, int size) {
		int offset = (page-1) * size;
		List<Customer> customers = customerRepository.findCustomerPaginated(size, offset);
		return new PageImpl<>(customers, PageRequest.of(page, size), customerRepository.count());
	}

	@Override
	public Customer retrieveById(Long id) {
		return customerRepository.findCustomerById(id).orElseThrow(CustomerNotFoundException::new);
//		return customerRepository.findCustomerByIdJoinFetch(id)
//			.orElseThrow(CustomerNotFoundException::new);
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

	public CustomerRegistration registerCustomer(CustomerRegistration customerRegistration) {
		customerRegistration.setCode(new RandomCode(RandomCodeType.ALPHANUMERIC, 4).toString());
		customerRegistration.setPassword(new BCryptPasswordEncoder().encode(customerRegistration.getPassword()));

		return customerRegistrationRepository.save(customerRegistration);
	}

	@Override
	@Transactional
	public Customer confirmRegistration(Long registrationId, String code) {
		CustomerRegistration registrationFound = customerRegistrationRepository.findByIdAndCode(registrationId, code)
			.orElseThrow(InvalidRegistrationCode::new);

		Customer customer = Customer.builder()
			.name(registrationFound.getName())
			.cpf(registrationFound.getCpf())
			.password(new BCryptPasswordEncoder().encode(registrationFound.getPassword()))
			.build();

		Customer createdCustomer = createCustomer(customer);

		customerRegistrationRepository.deleteById(registrationId);

		return createdCustomer;
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

		BankAccount createdBankAccount = null;

		for (BankAccount bankAccount : generatedCustomer.getBankAccounts()) {
			bankAccount.setOwner(createdCustomer);
			bankAccount.setPixKeys(List.of());
			createdBankAccount = bankAccountService.addBankAccount(bankAccount);
		}

		for (CreditCard creditCard : generatedCustomer.getCreditCards()) {
			creditCard.setCustomer(createdCustomer);
			creditCardService.addCreditCard(creditCard);
		}

		pixKeyRepository.save(new PixKey(customer.getCpf(), PixTypeEnum.CPF, createdBankAccount));

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

	private List<ConsentRole> getDefaultConsentRoles() {
		ConsentRole readAccounts = new ConsentRole();
		readAccounts.setId(1L);

		ConsentRole readTransactions = new ConsentRole();
		readTransactions.setId(2L);

		ConsentRole writePayments = new ConsentRole();
		writePayments.setId(3L);

		ConsentRole manageConsents = new ConsentRole();
		manageConsents.setId(4L);

		ConsentRole readBalances = new ConsentRole();
		readBalances.setId(5L);

		return List.of(readAccounts, readTransactions, writePayments, manageConsents, readBalances);
	}

	private BankAccount generateFakeBankAccount(Customer owner, BankName bankName, BankAccountType type) {
		Consent consent = new Consent();
		consent.setCustomer(owner);
		consent.setCode(UUID.randomUUID().toString());
		consent.setRoles(getDefaultConsentRoles());

		return new BankAccount(owner, new Bank(bankName), type, generateRandomBalance(), generateRandomAgency(), generateRandomNumber(), consent);
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
