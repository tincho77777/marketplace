package com.rest.marketplace.infrastructure.gateways.clients.exchangerate.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExchangeRateResponseTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void debeDeserializarJsonCorrectamente() throws Exception {
		String json = """
				{
				  "result": "success",
				  "base_code": "ARS",
				  "conversion_rates": {
				    "USD": 0.00089,
				    "EUR": 0.00082
				  }
				}
				""";

		ExchangeRateResponse response = objectMapper.readValue(json, ExchangeRateResponse.class);

		assertThat(response.getResult()).isEqualTo("success");
		assertThat(response.getBaseCode()).isEqualTo("ARS");
		assertThat(response.getConversionRates()).containsEntry("USD", 0.00089);
		assertThat(response.getConversionRates()).containsEntry("EUR", 0.00082);
	}

	@Test
	void debeCrearInstanciaVaciaConConstructorPorDefecto() {
		ExchangeRateResponse response = new ExchangeRateResponse();

		assertThat(response.getResult()).isNull();
		assertThat(response.getBaseCode()).isNull();
		assertThat(response.getConversionRates()).isNull();
	}

	@Test
	void debeDeserializarConversionRatesVacioCorrectamente() throws Exception {
		String json = """
				{
				  "result": "success",
				  "base_code": "USD",
				  "conversion_rates": {}
				}
				""";

		ExchangeRateResponse response = objectMapper.readValue(json, ExchangeRateResponse.class);

		assertThat(response.getResult()).isEqualTo("success");
		assertThat(response.getBaseCode()).isEqualTo("USD");
		assertThat(response.getConversionRates()).isInstanceOf(Map.class).isEmpty();
	}
}

