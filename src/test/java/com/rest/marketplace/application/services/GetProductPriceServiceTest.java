package com.rest.marketplace.application.services;

import com.rest.marketplace.application.services.product.GetProductPriceService;
import com.rest.marketplace.domain.exceptions.ProductNotFoundException;
import com.rest.marketplace.domain.ports.exchangerate.ExchangeRatePort;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import com.rest.marketplace.infrastructure.rest.product.response.ProductPriceResponse;
import com.rest.marketplace.utilities.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProductPriceServiceTest {

	@Mock
	private ProductPersistencePort productPersistencePort;

	@Mock
	private ExchangeRatePort exchangeRatePort;

	@InjectMocks
	private GetProductPriceService getProductPriceService;

	@Test
	void debeRetornarPrecioConvertidoCuandoElProductoExiste() {
		// ARRANGE
		Long id = TestData.PRODUCT_ID;
		var producto = TestData.productoDominio(); // precio: 150000 ARS
		BigDecimal usdRate = new BigDecimal("0.00089");

		when(productPersistencePort.findById(id)).thenReturn(Optional.of(producto));
		when(exchangeRatePort.getUsdRate()).thenReturn(usdRate);

		// ACT
		ProductPriceResponse resultado = getProductPriceService.getProductPrice(id);

		// ASSERT
		BigDecimal priceUsdEsperado = producto.getPrice()
				.multiply(usdRate)
				.setScale(2, RoundingMode.HALF_UP);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getId()).isEqualTo(producto.getId());
		assertThat(resultado.getTitle()).isEqualTo(producto.getTitle());
		assertThat(resultado.getPriceArs()).isEqualByComparingTo(producto.getPrice());
		assertThat(resultado.getPriceUsd()).isEqualByComparingTo(priceUsdEsperado); // 133.50 USD

		verify(productPersistencePort, times(1)).findById(id);
		verify(exchangeRatePort, times(1)).getUsdRate();
	}

	@Test
	void debeAplicarRedondeoHalfUpAlCalcularPrecioEnUsd() {
		// ARRANGE: precio que genera decimales que requieren redondeo
		Long id = TestData.PRODUCT_ID;
		var producto = TestData.productoDominio(); // precio: 150000 ARS
		BigDecimal usdRate = new BigDecimal("0.000897"); // 150000 * 0.000897 = 134.55

		when(productPersistencePort.findById(id)).thenReturn(Optional.of(producto));
		when(exchangeRatePort.getUsdRate()).thenReturn(usdRate);

		// ACT
		ProductPriceResponse resultado = getProductPriceService.getProductPrice(id);

		// ASSERT
		BigDecimal priceUsdEsperado = producto.getPrice()
				.multiply(usdRate)
				.setScale(2, RoundingMode.HALF_UP);

		assertThat(resultado.getPriceUsd()).isEqualByComparingTo(priceUsdEsperado);
		assertThat(resultado.getPriceUsd().scale()).isEqualTo(2);
	}

	@Test
	void debeLanzarProductNotFoundExceptionCuandoElProductoNoExiste() {
		// ARRANGE
		Long id = TestData.PRODUCT_ID_INEXISTENTE;

		when(productPersistencePort.findById(id)).thenReturn(Optional.empty());

		// ACT & ASSERT
		assertThrows(ProductNotFoundException.class,
				() -> getProductPriceService.getProductPrice(id));

		verify(productPersistencePort, times(1)).findById(id);
		// No debe consultar el tipo de cambio si el producto no existe
		verify(exchangeRatePort, never()).getUsdRate();
	}
}

