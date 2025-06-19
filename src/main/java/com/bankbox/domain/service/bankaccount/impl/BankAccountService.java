package com.bankbox.domain.service.bankaccount.impl;

import com.bankbox.domain.entity.*;
import com.bankbox.domain.service.bankaccount.PersistBankAccount;
import com.bankbox.domain.service.consent.ConsentServiceImpl;
import com.bankbox.domain.service.customer.RetrieveCustomer;
import com.bankbox.domain.exception.BankAccountNotFoundException;
import com.bankbox.domain.exception.CustomerAlreadyHasBankException;
import com.bankbox.infra.repository.BankAccountRepository;
import com.bankbox.domain.service.bankaccount.RetrieveBankAccount;
import com.bankbox.infra.repository.BankRepository;
import com.bankbox.infra.repository.ConsentRepository;
import lombok.RequiredArgsConstructor;
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
public class BankAccountService implements PersistBankAccount, RetrieveBankAccount {

	private final BankAccountRepository bankAccountRepository;
	private final ConsentRepository consentRepository;
	private final ConsentServiceImpl consentService;
	private final BankRepository bankRepository;

	@Override
	@Transactional
	public BankAccount addBankAccount(BankAccount bankAccount) {
		Customer customer = bankAccount.getOwner();
		List<BankAccount> customerAccounts = bankAccountRepository.findByOwnerId(customer.getId());
		validateCustomerDoesNotHaveBank(customerAccounts, bankAccount.getBank());

		Bank bankFound = bankRepository.findById(bankAccount.getBank().getId()).orElseThrow(BankAccountNotFoundException::new);

		BankAccount account = generateFakeBankAccount(bankAccount.getBank(), BankAccountType.CHECKING);

		Consent consent = new Consent();
		consent.setRoles(getDefaultConsentRoles());
		consent.setCode(UUID.randomUUID().toString());
		consent.setCustomer(customer);
		consent.setBank(bankFound);

		account.setOwner(customer);
		account.setConsent(consent);
		account.setBank(bankFound);

		bankAccountRepository.save(account);

		return bankAccountRepository.retrieveLastCreated();
	}

	@Override
	public BankAccount retrieveByPixKey(String pixKey) {
		return bankAccountRepository.findByPixKey(pixKey).
			orElseThrow(BankAccountNotFoundException::new);
	}

	private void validateCustomerDoesNotHaveBank(List<BankAccount> customerAccounts, Bank bank) {
		boolean customerHasBank = customerAccounts.stream()
			.anyMatch(bankAccount -> bankAccount.getBank().getName().equals(bank.getName()));

		if (customerHasBank) {
			throw new CustomerAlreadyHasBankException();
		}
	}

	@Override
	public BankAccount retrieveById(Long id) {
		return bankAccountRepository.findById(id).orElseThrow(BankAccountNotFoundException::new);
	}

	@Override
	public List<BankAccount> retrieveByUser(Long id) {
		return bankAccountRepository.findByOwnerId(id);
	}

	@Override
	public BankAccount retrieveByAgencyAndAccount(String bank, String agency, String account) {
		Optional<BankAccount> bankAccountFound = bankAccountRepository.findByBankNameAndAgencyAndAccount(BankName.valueOf(bank), agency, account);
		if (bankAccountFound.isEmpty()) throw new BankAccountNotFoundException();
		return bankAccountFound.get();
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

	private BankAccount generateFakeBankAccount(Bank bank, BankAccountType type) {
		return new BankAccount(null, bank, type, generateRandomBalance(), generateRandomAgency(), generateRandomNumber(), null);
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
