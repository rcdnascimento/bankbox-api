package com.bankbox.domain.exception;

public class CustomerNotFoundException extends RuntimeException {
	public CustomerNotFoundException() {
		super("Customer not found");
	}
}
