package com.rest.marketplace.application.services;

import com.rest.marketplace.application.usecases.product.GetProductsUc;
import com.rest.marketplace.domain.enums.product.ProductSortField;
import com.rest.marketplace.domain.enums.product.SortDirection;
import com.rest.marketplace.domain.models.common.PaginationRequest;
import com.rest.marketplace.infrastructure.rest.common.response.PageResponse;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductsService implements GetProductsUc {

	private final ProductPersistencePort productPersistencePort;

	@Override
	public PageResponse<Product> getProducts(PaginationRequest request) {
		var sortFieldValidated = ProductSortField.from(request.getSort());
		var sortDirectionValidated = SortDirection.from(request.getDirection());
		return productPersistencePort.findAll(request, sortFieldValidated, sortDirectionValidated);
	}

}
