package com.rest.marketplace.infrastructure.gateways.clients.exchangerate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class ExchangeRateResponse {

	@JsonProperty("result")
	private String result;

	@JsonProperty("base_code")
	private String baseCode;

	@JsonProperty("conversion_rates")
	private Map<String, Double> conversionRates;
}
