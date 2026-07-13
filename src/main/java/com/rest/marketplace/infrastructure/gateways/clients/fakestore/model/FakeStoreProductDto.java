package com.rest.marketplace.infrastructure.gateways.clients.fakestore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class FakeStoreProductDto {

	@JsonProperty("id")
	private Long id;

	@JsonProperty("title")
	private String title;

	@JsonProperty("price")
	private BigDecimal price;

	@JsonProperty("description")
	private String description;

	@JsonProperty("category")
	private String category;
}
