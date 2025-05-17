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
	private final RetrieveCustomer retrieveCustomer;

	public BankAccountService(BankAccountRepository bankAccountRepository, RetrieveCustomer retrieveCustomer) {
		this.bankAccountRepository = bankAccountRepository;
		this.retrieveCustomer = retrieveCustomer;
	}

	@Override
	public BankAccount saveBankAccount(BankAccount bankAccount) {
		Customer customer = retrieveCustomer.retrieveById(bankAccount.getOwner().getId());
		validateCustomerDoesNotHaveBank(customer, bankAccount.getBankName());
		BankAccount generatedBankAccount = generateFakeBankAccount(customer, bankAccount.getBankName(), BankAccountType.CHECKING);
		return bankAccountRepository.save(generatedBankAccount);
	}

	@Override
	public BankAccount retrieveByPixKey(String pixKey) {
		Optional<BankAccount> bankAccount = bankAccountRepository.findByPixKey(pixKey);
		if (bankAccount.isEmpty()) throw new BankAccountNotFoundException();
		return bankAccount.get();
	}

	private void validateCustomerDoesNotHaveBank(Customer customer, BankName bankName) {
		boolean customerHasBank = customer.getBankAccounts().stream()
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

	private BankAccount generateFakeBankAccount(Customer owner, BankName bankName, BankAccountType type) {
		return new BankAccount(owner, bankName, type, generateRandomBalance(), generateRandomAgency(), generateRandomNumber());
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
