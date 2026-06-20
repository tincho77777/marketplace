package com.rest.marketplace.domain.enums.product;

import com.rest.marketplace.domain.exceptions.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ProductSortField {

	ID("id"),
	TITLE("title"),
	PRICE("price"),
	STOCK("stock");

	private final String field;

	public static ProductSortField from(String value){
		return Arrays.stream(values())
				.filter(sort -> sort.field.equalsIgnoreCase(value))
				.findFirst()
				.orElseThrow(() -> new BadRequestException("Campo de ordeamiento invalido" + value));
	}
}
