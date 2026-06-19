package com.rest.marketplace.domain.enums.product;

import com.rest.marketplace.domain.exceptions.InvalidCategoryException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Category {

	TECH("tecnologia"),
	HOME("hogar"),
	SPORTS("deportes"),
	BOOKS("libros"),
	COCINA("cocina");

	private final String description;

	public static Category fromDescription(String description) {
		return Arrays.stream(values())
				.filter(category ->
						category.description.equalsIgnoreCase(description))
				.findFirst()
				.orElseThrow(() ->
						new InvalidCategoryException(description));
	}
}
