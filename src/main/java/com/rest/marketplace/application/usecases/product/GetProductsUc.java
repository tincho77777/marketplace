package com.rest.marketplace.application.usecases.product;

import com.rest.marketplace.domain.models.common.PaginationRequest;
import com.rest.marketplace.infrastructure.rest.common.response.PageResponse;
import com.rest.marketplace.domain.models.product.Product;

public interface GetProductsUc {

	PageResponse<Product> getProducts(PaginationRequest request);
}
