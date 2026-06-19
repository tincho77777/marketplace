package com.rest.marketplace.application.usecases.product;

import com.rest.marketplace.domain.models.product.Product;
import jakarta.validation.Valid;

public interface CreateProductUc {

	Product createProduct(@Valid Product product);
}
