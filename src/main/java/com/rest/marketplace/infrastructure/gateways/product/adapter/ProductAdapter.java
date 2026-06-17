package com.rest.marketplace.infrastructure.gateways.product.adapter;

import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import com.rest.marketplace.infrastructure.gateways.product.mapper.ProductMapper;
import com.rest.marketplace.infrastructure.gateways.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductAdapter implements ProductPersistencePort {

	private final ProductRepository productRepository;

	@Override
	public Optional<Product> findById(Long id) {
		return productRepository.findById(id).map(ProductMapper::toDomain);
	}

	@Override
	public List<Product> findAll() {
		return productRepository.findAll().stream().map(ProductMapper::toDomain).toList();
	}

	@Override
	public Product save(Product product) {
		var entity = ProductMapper.toEntity(product);
		var entitySaved = productRepository.save(entity);
		return ProductMapper.toDomain(entitySaved);
	}
}
