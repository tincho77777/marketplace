package com.rest.marketplace.application.services;

import com.rest.marketplace.application.usecases.product.GetProductUc;
import com.rest.marketplace.domain.exceptions.ProductNotFoundException;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductService implements GetProductUc {

	private final ProductPersistencePort productPersistencePort;

	@Cacheable(value = "product", key = "#id")
	@Override
	public Product getProductById(Long id) {
		return productPersistencePort.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
	}
}
