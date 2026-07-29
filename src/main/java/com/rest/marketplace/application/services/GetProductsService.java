package com.rest.marketplace.application.services;

import com.rest.marketplace.application.usecases.product.GetProductsUc;
import com.rest.marketplace.domain.enums.product.ProductSortField;
import com.rest.marketplace.domain.enums.product.SortDirection;
import com.rest.marketplace.domain.exceptions.RateLimitException;
import com.rest.marketplace.domain.exceptions.ServiceBusyException;
import com.rest.marketplace.domain.models.common.PaginationRequest;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import com.rest.marketplace.infrastructure.rest.common.response.PageResponse;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetProductsService implements GetProductsUc {

	private final ProductPersistencePort productPersistencePort;

	@Cacheable(
			value = "products",
			key = "#request.page + '-' + #request.size + '-' + #request.sort + '-' + #request.direction"
	)
	@RateLimiter(name = "products", fallbackMethod = "rateLimitFallback")
	@Bulkhead(name = "products" , fallbackMethod = "bulkheadFallback")
	@Override
	public PageResponse<Product> getProducts(PaginationRequest request) {
		var sortFieldValidated = ProductSortField.from(request.getSort());
		var sortDirectionValidated = SortDirection.from(request.getDirection());
		return productPersistencePort.findAll(request, sortFieldValidated, sortDirectionValidated);
	}

	public PageResponse<Product> rateLimitFallback(PaginationRequest request, RequestNotPermitted exception) {
		log.warn("⚠️ Rate limit alcanzado para getProducts");
		throw new RateLimitException("Limite de requests alcanzado para consulta de productos");
	}

	public PageResponse<Product> bulkheadFallback(PaginationRequest request, BulkheadFullException ex) {
		log.warn("⚠️ Bulkhead lleno para getProducts");
		throw new ServiceBusyException("Servicio ocupado, reintenta en unos segundos");
	}

}
