package com.rest.marketplace.infrastructure.rest.product.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImportProductsResponse {

	private int importedCount;
	private String message;
}
