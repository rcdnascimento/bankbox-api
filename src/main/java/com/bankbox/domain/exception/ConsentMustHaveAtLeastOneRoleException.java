package com.bankbox.domain.exception;

public class ConsentMustHaveAtLeastOneRoleException extends RuntimeException {
  public ConsentMustHaveAtLeastOneRoleException() {
    super("Consent must have at least one role");
  }
}
