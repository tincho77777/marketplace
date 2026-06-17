package com.rest.marketplace.application.services;

import com.rest.marketplace.application.usecases.product.GetProductUseCase;
import com.rest.marketplace.domain.exceptions.ProductNotFoundException;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductService implements GetProductUseCase {

	private final ProductPersistencePort productPersistencePort;

	@Override
	public Product execute(Long id) {
		return productPersistencePort.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
	}
}
