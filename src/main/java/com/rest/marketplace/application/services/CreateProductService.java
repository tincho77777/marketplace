package com.rest.marketplace.application.services;

import com.rest.marketplace.application.usecases.product.CreateProductUc;
import com.rest.marketplace.domain.models.events.ProductCreatedEvent;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.domain.ports.outbox.OutboxPort;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateProductService implements CreateProductUc {

	private final ProductPersistencePort productPersistencePort;
	private final OutboxPort outboxPort;

	@Caching(evict = {
			@CacheEvict(value = "products", allEntries = true)
	})
	@Override
	@Transactional
	public Product createProduct(Product product) {
		var saved = productPersistencePort.save(product);

		outboxPort.saveProductCreatedEvent(
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
