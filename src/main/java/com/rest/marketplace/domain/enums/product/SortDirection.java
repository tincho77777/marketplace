package com.rest.marketplace.domain.enums.product;


import com.rest.marketplace.domain.exceptions.BadRequestException;

import java.util.Arrays;

public enum SortDirection {

	ASC,
	DESC;

	public static SortDirection from(String value) {
		return Arrays.stream(values())
				.filter(direction -> direction.name().equalsIgnoreCase(value))
				.findFirst()
				.orElseThrow(() -> new BadRequestException("Dirección inválida: " + value));
	}
}
