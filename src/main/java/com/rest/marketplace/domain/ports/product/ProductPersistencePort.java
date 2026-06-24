package com.rest.marketplace.domain.ports.product;

import com.rest.marketplace.domain.enums.product.ProductSortField;
import com.rest.marketplace.domain.enums.product.SortDirection;
import com.rest.marketplace.domain.models.common.PaginationRequest;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.infrastructure.rest.common.response.PageResponse;

import java.util.Optional;

public interface ProductPersistencePort {

	Optional<Product> findById(Long id);

	Product save(Product product);

	PageResponse<Product> findAll(PaginationRequest paginationRequest, ProductSortField sortField, SortDirection sortDirection);

	void deleteById(Long id);
}
