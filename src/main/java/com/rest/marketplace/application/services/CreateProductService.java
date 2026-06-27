package com.rest.marketplace.application.services;

import com.rest.marketplace.application.usecases.product.CreateProductUc;
import com.rest.marketplace.domain.models.events.ProductCreatedEvent;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.domain.ports.out.ProductEventPort;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateProductService implements CreateProductUc {

	private final ProductPersistencePort productPersistencePort;
	private final ProductEventPort productEventPort;

	@Caching(evict = {
			@CacheEvict(value = "products", allEntries = true)
	})
	@Override
	public Product createProduct(Product product) {
		var saved = productPersistencePort.save(product);

		productEventPort.publishProductCreated(
				ProductCreatedEvent.builder()
						.id(saved.getId())
						.title(saved.getTitle())
						.price(saved.getPrice())
						.category(saved.getCategory())
						.createdAt(LocalDateTime.now())
						.build()
		);
		return saved;
	}
}
