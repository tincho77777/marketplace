package com.rest.marketplace.domain.ports.fakestore;

import com.rest.marketplace.domain.models.product.Product;

import java.util.List;

public interface FakeStorePort {

	List<Product> importProducts();
}
