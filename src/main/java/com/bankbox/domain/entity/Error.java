package com.bankbox.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class Error {
	private final HttpStatus status;
	private final String message;
	private final String code;
}
