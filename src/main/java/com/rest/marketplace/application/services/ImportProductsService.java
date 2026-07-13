package com.rest.marketplace.application.services;

import com.rest.marketplace.application.usecases.product.ImportProductsUc;
import com.rest.marketplace.domain.ports.fakestore.FakeStorePort;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportProductsService implements ImportProductsUc {

	private final FakeStorePort fakeStorePort;
	private final ProductPersistencePort productPersistencePort;

	@Override
	@Transactional
	public int importFromFakeStore() {
		var products = fakeStorePort.importProducts();
		products.forEach(productPersistencePort::save);
		log.info("✅ {} productos importados desde FakeStore", products.size());
		return products.size();
	}
}
