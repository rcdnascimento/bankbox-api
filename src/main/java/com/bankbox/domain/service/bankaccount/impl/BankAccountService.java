package com.bankbox.domain.service.bankaccount.impl;

import com.bankbox.domain.entity.BankAccount;
import com.bankbox.domain.entity.BankAccountType;
import com.bankbox.domain.entity.BankName;
import com.bankbox.domain.entity.Customer;
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
		validateCustomerDoesNotHaveBank(customerAccounts, bankAccount.getBankName());

		BankAccount account = generateFakeBankAccount(bankAccount.getBankName(), BankAccountType.CHECKING);
		bankAccountRepository.insert(
			account.getBankName().name(),
			account.getAgency(),
			account.getAccount(),
			account.getType().name(),
			account.getBalance(),
			customerId
		);

		return bankAccountRepository.retrieveLastCreated();
	}

	@Override
	public BankAccount retrieveByPixKey(String pixKey) {
		return bankAccountRepository.findByPixKey(pixKey).
			orElseThrow(BankAccountNotFoundException::new);
	}

	private void validateCustomerDoesNotHaveBank(List<BankAccount> customerAccounts, BankName bankName) {
		boolean customerHasBank = customerAccounts.stream()
			.anyMatch(bankAccount -> bankAccount.getBankName() == bankName);

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

	private BankAccount generateFakeBankAccount(BankName bankName, BankAccountType type) {
		return new BankAccount(null, bankName, type, generateRandomBalance(), generateRandomAgency(), generateRandomNumber());
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
