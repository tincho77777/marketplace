package com.rest.marketplace.domain.ports.product;

import com.rest.marketplace.domain.models.common.PaginationRequest;
import com.rest.marketplace.infrastructure.rest.common.response.PageResponse;
import com.rest.marketplace.domain.models.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductPersistencePort {

	Optional<Product> findById(Long id);

	List<Product> findAll();

	Product save(Product product);

	PageResponse<Product> findAll(PaginationRequest paginationRequest);
}
