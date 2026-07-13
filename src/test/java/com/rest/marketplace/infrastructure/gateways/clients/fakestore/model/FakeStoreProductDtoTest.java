package com.rest.marketplace.infrastructure.gateways.clients.fakestore.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class FakeStoreProductDtoTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void debeDeserializarJsonCorrectamente() throws Exception {
		String json = """
				{
				  "id": 1,
				  "title": "Laptop gamer",
				  "price": 999.99,
				  "description": "Laptop de alta gama",
				  "category": "electronics"
				}
				""";

		FakeStoreProductDto dto = objectMapper.readValue(json, FakeStoreProductDto.class);

		assertThat(dto.getId()).isEqualTo(1L);
		assertThat(dto.getTitle()).isEqualTo("Laptop gamer");
		assertThat(dto.getPrice()).isEqualByComparingTo(new BigDecimal("999.99"));
		assertThat(dto.getDescription()).isEqualTo("Laptop de alta gama");
		assertThat(dto.getCategory()).isEqualTo("electronics");
	}

	@Test
	void debeCrearInstanciaVaciaConConstructorPorDefecto() {
		FakeStoreProductDto dto = new FakeStoreProductDto();

		assertThat(dto.getId()).isNull();
		assertThat(dto.getTitle()).isNull();
		assertThat(dto.getPrice()).isNull();
		assertThat(dto.getDescription()).isNull();
		assertThat(dto.getCategory()).isNull();
	}

	@Test
	void debeDeserializarCamposNulosCorrectamente() throws Exception {
		String json = """
				{
				  "id": null,
				  "title": null,
				  "price": null,
				  "description": null,
				  "category": null
				}
				""";

		FakeStoreProductDto dto = objectMapper.readValue(json, FakeStoreProductDto.class);

		assertThat(dto.getId()).isNull();
		assertThat(dto.getTitle()).isNull();
		assertThat(dto.getPrice()).isNull();
		assertThat(dto.getDescription()).isNull();
		assertThat(dto.getCategory()).isNull();
	}
}

