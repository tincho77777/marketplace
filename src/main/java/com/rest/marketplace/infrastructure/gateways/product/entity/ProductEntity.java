package com.rest.marketplace.infrastructure.gateways.product.entity;

import com.rest.marketplace.domain.enums.product.Category;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String description;

	private BigDecimal price;

	private Integer stock;

	@Enumerated(EnumType.STRING)
	private Category category;
}
