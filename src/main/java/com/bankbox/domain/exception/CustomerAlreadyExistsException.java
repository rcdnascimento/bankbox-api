package com.bankbox.domain.exception;

public class CustomerAlreadyExistsException extends RuntimeException {
	public CustomerAlreadyExistsException() {
		super("Customer already exists");
	}
}
