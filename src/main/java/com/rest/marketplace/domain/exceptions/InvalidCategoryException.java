package com.rest.marketplace.domain.exceptions;

public class InvalidCategoryException extends RuntimeException {

	public InvalidCategoryException(String category) {
		super("Categoría inválida: " + category);
	}
}
