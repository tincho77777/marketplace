package com.rest.marketplace.application.services;

import com.rest.marketplace.application.usecases.product.UpdateProductUc;
import com.rest.marketplace.domain.exceptions.ProductNotFoundException;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProductService implements UpdateProductUc {

	private final ProductPersistencePort productPersistencePort;

	@Caching(
			put = @CachePut(value = "product", key = "#result.id"),
			evict = @CacheEvict(value = "products", allEntries = true)
	)
	@Override
	public Product updateProduct(Long id, Product product) {
		var existingProduct = productPersistencePort.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		existingProduct.setTitle(product.getTitle());
		existingProduct.setDescription(product.getDescription());
		existingProduct.setPrice(product.getPrice());
		existingProduct.setStock(product.getStock());
		existingProduct.setCategory(product.getCategory());

		return productPersistencePort.save(existingProduct);
	}
}
