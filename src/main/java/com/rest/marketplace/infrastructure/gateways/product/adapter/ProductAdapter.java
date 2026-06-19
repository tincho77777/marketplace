package com.rest.marketplace.infrastructure.gateways.product.adapter;

import com.rest.marketplace.domain.models.common.PaginationRequest;
import com.rest.marketplace.infrastructure.rest.common.response.PageResponse;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import com.rest.marketplace.infrastructure.gateways.product.mapper.ProductMapper;
import com.rest.marketplace.infrastructure.gateways.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

	@Override
	public PageResponse<Product> findAll(PaginationRequest paginationRequest) {

		var pageable = PageRequest.of(
				paginationRequest.getPage(),
				paginationRequest.getSize()
		);

		var page = productRepository.findAll(pageable);

		return PageResponse.<Product>builder().content(
				page.getContent()
						.stream()
						.map(ProductMapper::toDomain)
						.toList()
				)
				.page(page.getNumber())
				.size(page.getSize())
				.totalElements(page.getTotalElements())
				.totalPages(page.getTotalPages())
				.build();
	}
}
