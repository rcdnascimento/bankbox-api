package com.bankbox.domain.service.bankaccount;

import com.bankbox.domain.entity.BankAccount;

public interface PersistBankAccount {
	public BankAccount saveBankAccount(BankAccount bankAccount);
}
