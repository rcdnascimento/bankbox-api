package com.bankbox.domain.service.bankaccount;

import com.bankbox.domain.entity.BankAccount;

import java.util.List;

public interface RetrieveBankAccount {
	BankAccount retrieveById(Long id);
	BankAccount retrieveByPixKey(String pixKey);
	List<BankAccount> retrieveByUser(Long id);
	BankAccount retrieveByAgencyAndAccount(String bank, String agency, String account);
}
