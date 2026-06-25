package com.rest.marketplace.domain.models.product;

import com.rest.marketplace.domain.enums.product.Category;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Product implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private Long id;
	private String title;
	private String description;
	private BigDecimal price;
	private Integer stock;
	private Category category;
}
