package com.rest.marketplace.domain.exceptions;

public class RateLimitException extends RuntimeException {

	public RateLimitException(String message) {
		super(message);
	}
}
