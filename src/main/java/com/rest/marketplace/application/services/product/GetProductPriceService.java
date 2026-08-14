package com.rest.marketplace.application.services.product;

import com.rest.marketplace.application.usecases.product.GetProductPriceUc;
import com.rest.marketplace.domain.exceptions.ProductNotFoundException;
import com.rest.marketplace.domain.ports.exchangerate.ExchangeRatePort;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import com.rest.marketplace.infrastructure.rest.product.response.ProductPriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class GetProductPriceService implements GetProductPriceUc {

	private final ProductPersistencePort productPersistencePort;
	private final ExchangeRatePort exchangeRatePort;

	@Override
	public ProductPriceResponse getProductPrice(Long id) {
		var product = productPersistencePort.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		var usdRate = exchangeRatePort.getUsdRate();
		var priceUsd = product.getPrice()
				.multiply(usdRate)
				.setScale(2, RoundingMode.HALF_UP);

		return ProductPriceResponse.builder()
				.id(product.getId())
				.title(product.getTitle())
				.priceArs(product.getPrice())
				.priceUsd(priceUsd)
				.build();
	}
}
