package com.rest.marketplace.infrastructure.rest.advice.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record ValidationErrorResponse(
		Integer code,
		String detail,
		String message,
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		LocalDateTime timestamp,
		String path,
		List<ValidationError> errors
) {
			public record ValidationError(
			String field,
			String message) {}
}
