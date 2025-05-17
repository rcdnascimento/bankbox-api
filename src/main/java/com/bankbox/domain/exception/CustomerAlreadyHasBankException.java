package com.bankbox.domain.exception;

public class CustomerAlreadyHasBankException extends RuntimeException {
	public CustomerAlreadyHasBankException() {
		super("Customer already has bank");
	}
}
