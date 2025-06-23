package com.bankbox.domain.exception;

public class InvalidRegistrationCode extends BusinessException {
	public InvalidRegistrationCode() {
		super("Registration code is invalid. Please check the code and try again.", "INVALID_REGISTRATION_CODE");
	}
}
