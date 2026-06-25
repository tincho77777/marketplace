package com.rest.marketplace.application.services;

import com.rest.marketplace.application.usecases.product.DeleteProductUc;
import com.rest.marketplace.domain.exceptions.ProductNotFoundException;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProductService implements DeleteProductUc {

	private final ProductPersistencePort productPersistencePort;

	@Caching(
			evict = {
					@CacheEvict(value = "product", key = "#idProducto"),
					@CacheEvict(value = "products", allEntries = true)
			}
	)
	@Override
	public void deleteProduct(Long idProducto) {
		productPersistencePort.findById(idProducto).orElseThrow(() -> new ProductNotFoundException(idProducto));
		productPersistencePort.deleteById(idProducto);
	}
}
