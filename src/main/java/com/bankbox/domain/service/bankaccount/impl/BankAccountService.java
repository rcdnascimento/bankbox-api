package com.bankbox.domain.service.bankaccount.impl;

import com.bankbox.domain.entity.*;
import com.bankbox.domain.service.bankaccount.PersistBankAccount;
import com.bankbox.domain.service.customer.RetrieveCustomer;
import com.bankbox.domain.exception.BankAccountNotFoundException;
import com.bankbox.domain.exception.CustomerAlreadyHasBankException;
import com.bankbox.infra.repository.BankAccountRepository;
import com.bankbox.domain.service.bankaccount.RetrieveBankAccount;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class BankAccountService implements PersistBankAccount, RetrieveBankAccount {

	private final BankAccountRepository bankAccountRepository;

	public BankAccountService(BankAccountRepository bankAccountRepository) {
		this.bankAccountRepository = bankAccountRepository;
	}

	@Override
	public BankAccount addBankAccount(BankAccount bankAccount) {
		Long customerId = bankAccount.getOwner().getId();
		List<BankAccount> customerAccounts = bankAccountRepository.findByOwnerId(customerId);
		validateCustomerDoesNotHaveBank(customerAccounts, bankAccount.getBank());

		BankAccount account = generateFakeBankAccount(bankAccount.getBank(), BankAccountType.CHECKING);
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

	private BankAccount generateFakeBankAccount(Bank bank, BankAccountType type) {
		return new BankAccount(null, bank, type, generateRandomBalance(), generateRandomAgency(), generateRandomNumber());
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
