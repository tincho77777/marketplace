package com.rest.marketplace.domain.enums.product;

import com.rest.marketplace.domain.exceptions.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductSortFieldTest {

	@Test
	void debeRetornarIdCuandoElCampoEsId() {
		ProductSortField result = ProductSortField.from("id");

		assertThat(result).isEqualTo(ProductSortField.ID);
	}

	@Test
	void debeRetornarPriceIgnorandoMayusculasYMinusculas() {
		ProductSortField result = ProductSortField.from("PrIcE");

		assertThat(result).isEqualTo(ProductSortField.PRICE);
	}

	@Test
	void debeLanzarBadRequestExceptionCuandoElCampoEsInvalido() {
		BadRequestException exception = assertThrows(
				BadRequestException.class,
				() -> ProductSortField.from("category")
		);

		assertThat(exception.getMessage())
				.isEqualTo("Campo de ordenamiento invalido: category");
	}

}