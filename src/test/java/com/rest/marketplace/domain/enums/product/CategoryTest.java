package com.rest.marketplace.domain.enums.product;

import com.rest.marketplace.domain.exceptions.InvalidCategoryException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryTest {

	@Test
	void debeRetornarCategoriaCorrectaCuandoLaDescripcionEsValida() {

		Category category = Category.fromDescription("tecnologia");

		assertThat(category).isEqualTo(Category.TECH);
		assertThat(category.getDescription()).isEqualTo("tecnologia");
	}

	@Test
	void debeRetornarCategoriaCorrectaIgnorandoMayusculasYMinusculas() {

		Category category = Category.fromDescription("TeCnOlOgIa");

		assertThat(category).isEqualTo(Category.TECH);
	}

	@Test
	void debeRetornarTodasLasCategoriasCorrectamente() {

		assertThat(Category.fromDescription("tecnologia")).isEqualTo(Category.TECH);
		assertThat(Category.fromDescription("hogar")).isEqualTo(Category.HOME);
		assertThat(Category.fromDescription("deportes")).isEqualTo(Category.SPORTS);
		assertThat(Category.fromDescription("libros")).isEqualTo(Category.BOOKS);
		assertThat(Category.fromDescription("cocina")).isEqualTo(Category.COCINA);
	}

	@Test
	void debeLanzarExcepcionCuandoLaDescripcionNoExiste() {

		InvalidCategoryException exception = assertThrows(
				InvalidCategoryException.class,
				() -> Category.fromDescription("ropa")
		);

		assertThat(exception.getMessage()).isEqualTo("Categoría inválida: ropa");
	}
}