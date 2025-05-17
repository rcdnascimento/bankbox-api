package com.bankbox.domain.exception;

public class BalanceNotEnoughException extends RuntimeException {
	public BalanceNotEnoughException() {
		super("Source balance is not enough");
	}
}
