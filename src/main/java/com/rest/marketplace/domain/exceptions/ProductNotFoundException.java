package com.rest.marketplace.domain.exceptions;

public class ProductNotFoundException extends RuntimeException {

	public ProductNotFoundException(Long id) {
		super("Producto con id " + id + " no encontrado.");
	}
}
