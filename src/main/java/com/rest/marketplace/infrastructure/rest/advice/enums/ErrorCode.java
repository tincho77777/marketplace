package com.rest.marketplace.infrastructure.rest.advice.enums;

public enum ErrorCode {
	PRODUCT_NOT_FOUND,
	VALIDATION_ERROR,
	INTERNAL_SERVER_ERROR,
	INVALID_CATEGORY,
	BAD_REQUEST,
	OUTBOX_ERROR,
	SQS_ERROR
}
