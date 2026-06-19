package com.rest.marketplace.infrastructure.rest.product.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponse(
		@Schema(
				description = "Identificador único del producto",
				example = "1"
		)
		Long id,
		@Schema(
				description = "Nombre del producto",
				example = "Notebook Lenovo"
		)
		String title,
		@Schema(
				description = "Descripcion del producto",
				example = "Procesador ryzen 9"
		)
		String description,
		@Schema(
				description = "Precio del producto",
				example = "1500000.00"
		)
		BigDecimal price,
		@Schema(
				description = "Cantidad disponible del producto",
				example = "1234"
		)
		Integer stock,
		@Schema(
				description = "Categoria a la que pertenece el producto",
				example = "tecnologia"
		)
		String category) {
}
