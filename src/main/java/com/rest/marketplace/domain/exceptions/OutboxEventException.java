package com.rest.marketplace.domain.exceptions;

public class OutboxEventException extends RuntimeException {

	public OutboxEventException(String message, Throwable cause) {
		super(message, cause);
	}
}
