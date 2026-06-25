package com.rest.marketplace.application.services;

import com.rest.marketplace.application.usecases.product.CreateProductUc;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateProductService implements CreateProductUc {

	private final ProductPersistencePort productPersistencePort;

	@Caching(evict = {
			@CacheEvict(value = "products", allEntries = true)
	})
	@Override
	public Product createProduct(Product product) {
		return productPersistencePort.save(product);
	}
}
