package com.rest.marketplace.infrastructure.rest.product.request.update.mapper;

import com.rest.marketplace.domain.enums.product.Category;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.infrastructure.rest.product.request.update.UpdateProductRequest;

public class UpdateProductRequestMapper {

	private UpdateProductRequestMapper(){}

	public static Product toDomain (UpdateProductRequest request){
		return Product.builder()
				.title(request.title())
				.description(request.description())
				.price(request.price())
				.stock(request.stock())
				.category(Category.fromDescription(request.category()))
				.build();
	}
}
