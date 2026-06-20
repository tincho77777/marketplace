package com.rest.marketplace.application.usecases.product;

import com.rest.marketplace.domain.models.product.Product;

public interface UpdateProductUc {

	Product updateProduct(Long id, Product product);
}
