package com.rest.marketplace.infrastructure.rest.advice;

import com.rest.marketplace.domain.exceptions.InvalidCategoryException;
import com.rest.marketplace.domain.exceptions.ProductNotFoundException;
import com.rest.marketplace.infrastructure.rest.advice.enums.ErrorCode;
import com.rest.marketplace.infrastructure.rest.advice.response.ErrorResponse;
import com.rest.marketplace.infrastructure.rest.advice.response.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException exception,
	                                                           HttpServletRequest request){
		var status = HttpStatus.NOT_FOUND;
		var error = new ErrorResponse(
				status.value(),
				ErrorCode.PRODUCT_NOT_FOUND.name(),
				exception.getMessage(),
				LocalDateTime.now(),
				request.getRequestURI()
		);
		return ResponseEntity.status(status).body(error);
	}

	@ExceptionHandler(InvalidCategoryException.class)
	public ResponseEntity<ErrorResponse> handleInvalidCategory(InvalidCategoryException exception,
	                                                           HttpServletRequest request){
		var status = HttpStatus.BAD_REQUEST;
		var error = new ErrorResponse(
				status.value(),
				ErrorCode.INVALID_CATEGORY.name(),
				exception.getMessage(),
				LocalDateTime.now(),
				request.getRequestURI()
		);
		return ResponseEntity.status(status).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponse> handleArgumentNotValidException(MethodArgumentNotValidException exception,
	                                                                               HttpServletRequest request) {

		var errors = exception.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> new ValidationErrorResponse.ValidationError(
						error.getField(),
						error.getDefaultMessage()))
				.toList();

		var response = new ValidationErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				ErrorCode.VALIDATION_ERROR.name(),
				"Se encontraron errores de validación",
				LocalDateTime.now(),
				request.getRequestURI(),
				errors
		);

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ValidationErrorResponse> handleMethodValidation(HandlerMethodValidationException exception,
	                                                                      HttpServletRequest request) {

		var errors = exception.getParameterValidationResults()
				.stream()
				.flatMap(result ->
						result.getResolvableErrors().stream()
								.map(error -> new ValidationErrorResponse.ValidationError(
										result.getMethodParameter().getParameterName(),
										error.getDefaultMessage()
								))
				)
				.toList();

		var response = new ValidationErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				ErrorCode.VALIDATION_ERROR.name(),
				"La solicitud contiene errores de validación.",
				LocalDateTime.now(),
				request.getRequestURI(),
				errors
		);

		return ResponseEntity.badRequest().body(response);
	}
}
