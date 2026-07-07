package com.rest.marketplace.application.usecases.product;

import com.rest.marketplace.infrastructure.rest.product.response.ProductPriceResponse;

public interface GetProductPriceUc {

	ProductPriceResponse getProductPrice(Long id);
}
