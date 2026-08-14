package com.rest.marketplace.application.services.product;

import com.rest.marketplace.application.usecases.product.ImportProductsUc;
import com.rest.marketplace.domain.exceptions.RateLimitException;
import com.rest.marketplace.domain.ports.fakestore.FakeStorePort;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportProductsService implements ImportProductsUc {

	private final FakeStorePort fakeStorePort;
	private final ProductPersistencePort productPersistencePort;

	@Override
	@Transactional
	@RateLimiter(name = "import", fallbackMethod = "rateLimitImportFallback")
	@Bulkhead(name = "import", type = Bulkhead.Type.THREADPOOL, fallbackMethod = "bulkheadImportFallback")  // cuando usás Bulkhead.Type.THREADPOOL el servicio tiene que devolver CompletableFuture.
	public CompletableFuture<Integer> importFromFakeStore() {
		log.info("🚀 Iniciando importación paralela desde FakeStore");
		var startTime = System.currentTimeMillis();
		var products = fakeStorePort.importProducts();

		//crear un ComplteableFuture por cada producto
		List<CompletableFuture<Void>> futures = products.stream()
						.map(product -> CompletableFuture.runAsync(() -> {
							log.info("[hilo: {}] 💾 Guardando producto: {}", Thread.currentThread().getName(), product.getTitle());
							productPersistencePort.save(product);
		})).toList();

		// espera que todos los CompletableFuture completen antes de devolver la respuesta al cliente.
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

		var duration = System.currentTimeMillis() - startTime;
		log.info("✅ {} productos importados en {}ms (paralelo)", products.size(), duration);

		return CompletableFuture.completedFuture(products.size());
	}

	public CompletableFuture<Integer> rateLimitImportFallback(RequestNotPermitted exception) {
		log.warn("⚠️ Rate limit alcanzado para importación");
		throw new RateLimitException("Límite de importaciones alcanzado");
	}

	public CompletableFuture<Integer> bulkheadImportFallback(BulkheadFullException exception) {
		log.warn("⚠️ Bulkhead lleno para importación");
		return CompletableFuture.completedFuture(0);
	}
}
