package com.rest.marketplace.infrastructure.rest.product.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductPriceResponse {

	private Long id;
	private String title;
	@JsonProperty("price_ars")
	private BigDecimal priceArs;
	@JsonProperty("price_usd")
	private BigDecimal priceUsd;
}
