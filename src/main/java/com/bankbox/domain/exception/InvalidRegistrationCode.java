package com.bankbox.domain.exception;

public class InvalidRegistrationCode extends RuntimeException {
	public InvalidRegistrationCode() {
		super("Registration code is invalid. Please check the code and try again.");
	}
}
