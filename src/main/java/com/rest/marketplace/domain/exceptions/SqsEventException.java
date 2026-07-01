package com.rest.marketplace.domain.exceptions;

public class SqsEventException extends RuntimeException{

	public SqsEventException(String message, Throwable cause) {
		super(message, cause);
	}
}
