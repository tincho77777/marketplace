package com.rest.marketplace.domain.exceptions;

public class ServiceBusyException extends RuntimeException {

	public ServiceBusyException(String message) {
		super(message);
	}
}
