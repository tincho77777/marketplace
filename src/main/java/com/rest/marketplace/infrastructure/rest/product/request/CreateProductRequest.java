package com.rest.marketplace.infrastructure.rest.product.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateProductRequest(
		@NotBlank(message = "El titulo es obligatorio.")
		@Schema(
				description = "Nombre del producto",
				example = "Notebook Lenovo"
		)
		String title,
		@NotBlank(message = "La descripcion es obligatoria.")
		@Schema(
				description = "Descripcion del producto",
				example = "Procesador ryzen 9"
		)
		String description,
		@NotNull(message = "El precio no puede ser nulo")
		@Positive(message = "El importe del precio debe ser mayor a 0")
		@Schema(
				description = "Precio del producto",
				example = "1500000.00"
		)
		BigDecimal price,
		@NotNull(message = "El stock no puede ser nulo")
		@PositiveOrZero(message = "El stock tiene que ser igual o mayor a 0")
		@Schema(
				description = "Cantidad disponible del producto",
				example = "1234"
		)
		Integer stock,
		@NotBlank(message = "La categoria es obligatoria")
		@Schema(
				description = "Categoria a la que pertenece el producto",
				example = "tecnologia"
		)
		String category) {}
