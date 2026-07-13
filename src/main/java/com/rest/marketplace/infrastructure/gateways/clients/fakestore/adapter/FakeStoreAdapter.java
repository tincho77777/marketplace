package com.rest.marketplace.infrastructure.gateways.clients.fakestore.adapter;

import com.rest.marketplace.domain.enums.product.Category;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.domain.ports.fakestore.FakeStorePort;
import com.rest.marketplace.infrastructure.gateways.clients.fakestore.FakeStoreClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FakeStoreAdapter implements FakeStorePort {

	private final FakeStoreClient fakeStoreClient;

	@Override
	public List<Product> importProducts() {
		log.info("📦 Importando productos desde FakeStore...");

		return fakeStoreClient.getProducts()
				.stream()
				.map(dto -> Product.builder()
						.title(dto.getTitle())
						.description(dto.getDescription())
						.price(dto.getPrice())
						.stock(10) // stock por defecto
						.category(mapCategory(dto.getCategory()))
						.build()
				).toList();
	}

	private Category mapCategory(String category) {
		return switch (category.toLowerCase()) {
			case "electronics" -> Category.TECH;
			case "jewelery", "women's clothing", "men's clothing" -> Category.HOME;
			default -> Category.TECH;
		};
	}
}
