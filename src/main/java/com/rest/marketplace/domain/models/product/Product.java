package com.rest.marketplace.domain.models.product;

import com.rest.marketplace.domain.enums.product.Category;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Product {

	private Long id;
	private String title;
	private String description;
	private BigDecimal price;
	private Integer stock;
	private Category category;
}
