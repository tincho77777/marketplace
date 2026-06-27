package com.rest.marketplace.domain.models.events;

import com.rest.marketplace.domain.enums.product.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreatedEvent implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private Long id;
	private String title;
	private BigDecimal price;
	private Category category;
	private LocalDateTime createdAt;
}
