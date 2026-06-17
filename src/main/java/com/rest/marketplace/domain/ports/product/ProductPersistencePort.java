package com.rest.marketplace.domain.ports.product;

import com.rest.marketplace.domain.models.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductPersistencePort {

	Optional<Product> findById(Long id);

	List<Product> findAll();

	Product save(Product product);
}
