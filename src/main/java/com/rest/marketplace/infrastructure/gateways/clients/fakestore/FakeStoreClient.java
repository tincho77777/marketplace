package com.rest.marketplace.infrastructure.gateways.clients.fakestore;

import com.rest.marketplace.infrastructure.gateways.clients.fakestore.model.FakeStoreProductDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface FakeStoreClient {

	@GetExchange("/products")
	List<FakeStoreProductDto> getProducts();

	@GetExchange("/products/{id}")
	FakeStoreProductDto getProductById(@PathVariable Long id);
}
