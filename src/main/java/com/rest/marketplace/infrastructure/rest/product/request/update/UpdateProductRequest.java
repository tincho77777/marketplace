package com.rest.marketplace.infrastructure.rest.product.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateProductRequest(

		@NotBlank(message = "El titulo es obligatorio.")
		String title,

		@NotBlank(message = "La descripcion es obligatoria.")
		String description,

		@NotNull(message = "El precio no puede ser nulo")
		@Positive(message = "El importe del precio debe ser mayor a 0")
		BigDecimal price,

		@NotNull(message = "El stock no puede ser nulo")
		@PositiveOrZero(message = "El stock tiene que ser igual o mayor a 0")
		Integer stock,

		@NotBlank(message = "La categoria es obligatoria")
		String category
) {
}
