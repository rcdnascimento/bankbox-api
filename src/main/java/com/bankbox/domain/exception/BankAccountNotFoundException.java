package com.bankbox.domain.exception;

public class BankAccountNotFoundException extends RuntimeException {
	public BankAccountNotFoundException() {
		super("Bank account not found");
	}
}
