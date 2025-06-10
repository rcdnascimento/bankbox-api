package com.bankbox.domain.exception;

public class CreditCardNotFoundException extends RuntimeException {
	public CreditCardNotFoundException() {
		super("Credit cards not found");
	}
}
