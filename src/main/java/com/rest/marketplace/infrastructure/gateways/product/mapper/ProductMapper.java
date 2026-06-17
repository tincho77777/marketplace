package com.rest.marketplace.infrastructure.gateways.product.mapper;

import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.infrastructure.gateways.product.entity.ProductEntity;

public class ProductMapper {

	private ProductMapper(){}

	public static Product toDomain(ProductEntity entity){
		if(entity == null){
			return null;
		}
		return Product.builder()
				.id(entity.getId())
				.title(entity.getTitle())
				.description(entity.getDescription())
				.price(entity.getPrice())
				.stock(entity.getStock())
				.category(entity.getCategory())
				.build();
	}

	public static ProductEntity toEntity(Product product) {
		if (product == null) {
			return null;
		}
		return ProductEntity.builder()
				.id(product.getId())
				.title(product.getTitle())
				.description(product.getDescription())
				.price(product.getPrice())
				.stock(product.getStock())
				.category(product.getCategory())
				.build();
	}

}