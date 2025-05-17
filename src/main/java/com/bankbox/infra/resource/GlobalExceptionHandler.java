package com.bankbox.infra.resource;

import com.bankbox.domain.entity.Error;
import com.bankbox.domain.exception.BalanceNotEnoughException;
import com.bankbox.domain.exception.BankAccountNotFoundException;
import com.bankbox.domain.exception.CustomerAlreadyExistsException;
import com.bankbox.domain.exception.CustomerAlreadyHasBankException;
import com.bankbox.domain.exception.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Error handleValidationException(MethodArgumentNotValidException exception) {
			Map<String, String> errors = new HashMap<>();
			exception.getBindingResult().getAllErrors().forEach(error -> {
				String fieldName = ((FieldError) error).getField();
				String errorMessage = error.getDefaultMessage();
				errors.put(fieldName, errorMessage);
			});
			String message = errors.toString()
				.replace("=", " ")
				.replace("{", "")
				.replace("}", "");
			return new Error(HttpStatus.BAD_REQUEST, message, "ARGUMENT_NOT_VALID");
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(CustomerNotFoundException.class)
	public Error customerNotFoundException(CustomerNotFoundException exception) {
		return new Error(HttpStatus.NOT_FOUND, exception.getMessage(), "CUSTOMER_NOT_FOUND");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CustomerAlreadyExistsException.class)
	public Error customerAlreadyExistsException(CustomerAlreadyExistsException exception) {
		return new Error(HttpStatus.BAD_REQUEST, exception.getMessage(), "CUSTOMER_ALREADY_EXISTS");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CustomerAlreadyHasBankException.class)
	public Error customerAlreadyHasBankException(CustomerAlreadyHasBankException exception) {
		return new Error(HttpStatus.BAD_REQUEST, exception.getMessage(), "CUSTOMER_ALREADY_HAS_BANK");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BalanceNotEnoughException.class)
	public Error balanceNotEnough(BalanceNotEnoughException exception) {
		return new Error(HttpStatus.BAD_REQUEST, exception.getMessage(), "BALANCE_NOT_ENOUGH");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public Error illegalArgumentoException(IllegalArgumentException exception) {
		return new Error(HttpStatus.BAD_REQUEST, exception.getMessage(), "ILLEGAL_ARGUMENT");
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(BankAccountNotFoundException.class)
	public Error bankAccountNotFoundException(BankAccountNotFoundException exception) {
		return new Error(HttpStatus.NOT_FOUND, exception.getMessage(), "BANK_ACCOUNT_NOT_FOUND");
	}
}
