package com.rest.marketplace.infrastructure.rest.product.response.mapper;

import com.rest.marketplace.domain.enums.product.Category;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.infrastructure.rest.product.response.ProductResponse;

public class ProductResponseMapper {

	private ProductResponseMapper() {
	}

	public static ProductResponse toResponse(Product product) {
		return ProductResponse.builder()
				.id(product.getId())
				.title(product.getTitle())
				.description(product.getDescription())
				.price(product.getPrice())
				.stock(product.getStock())
				.category(buildCategory(product.getCategory()))
				.build();
	}

	public static String buildCategory(Category category){
		return switch (category){
			case TECH -> Category.TECH.getDescription();
			case HOME -> Category.HOME.getDescription();
			case BOOKS -> Category.BOOKS.getDescription();
			case SPORTS -> Category.SPORTS.getDescription();
			case COCINA -> Category.COCINA.getDescription();
		};
	}
}
