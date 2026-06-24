package com.rest.marketplace.domain.enums.product;

import com.rest.marketplace.domain.exceptions.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SortDirectionTest {

	@Test
	void debeRetornarAscCuandoElValorEsAsc() {
		SortDirection result = SortDirection.from("ASC");

		assertThat(result).isEqualTo(SortDirection.ASC);
	}

	@Test
	void debeRetornarDescCuandoElValorEsDesc() {
		SortDirection result = SortDirection.from("DESC");

		assertThat(result).isEqualTo(SortDirection.DESC);
	}

	@Test
	void debeLanzarBadRequestExceptionCuandoLaDireccionEsInvalida() {
		BadRequestException exception = assertThrows(
				BadRequestException.class,
				() -> SortDirection.from("INVALID")
		);

		assertThat(exception.getMessage()).isEqualTo("Dirección inválida: INVALID");
	}

	@Test
	void debeRetornarAscIgnorandoMayusculasYMinusculas() {
		SortDirection result = SortDirection.from("asc");

		assertThat(result).isEqualTo(SortDirection.ASC);
	}

}