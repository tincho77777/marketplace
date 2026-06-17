package com.rest.marketplace.application.usecases.product;

import com.rest.marketplace.domain.models.product.Product;

public interface GetProductUseCase {

	Product execute(Long id);
}
