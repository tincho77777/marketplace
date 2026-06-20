package com.rest.marketplace.infrastructure.rest.product.request.create.mapper;

import com.rest.marketplace.domain.enums.product.Category;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.infrastructure.rest.product.request.create.CreateProductRequest;

public class CreateProductRequestMapper {

	private CreateProductRequestMapper(){}

	public static Product toDomain (CreateProductRequest request){
		return Product.builder()
				.title(request.title())
				.description(request.description())
				.price(request.price())
				.stock(request.stock())
				.category(Category.fromDescription(request.category()))
				.build();
	}
}
