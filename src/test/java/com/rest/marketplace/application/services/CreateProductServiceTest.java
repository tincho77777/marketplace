package com.rest.marketplace.application.services;

import com.rest.marketplace.domain.enums.product.Category;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import com.rest.marketplace.utilities.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductServiceTest {

	@Mock
	private ProductPersistencePort productPersistencePort;

	@InjectMocks
	private CreateProductService createProductService;

	@Test
	void debeCrearUnProductoCuandoHayDatosValidos(){
		Product productoSinId = TestData.productoDominioSinId();
		Product productoGuardado = TestData.productoDominio();

		when(productPersistencePort.save(productoSinId)).thenReturn(productoGuardado);

		Product resultado = createProductService.createProduct(productoSinId);

		assertThat(resultado.getId()).isNotNull();
		assertThat(resultado)
				.extracting(Product::getTitle, Product::getDescription, Product::getPrice, Product::getStock, Product::getCategory)
				.containsExactly("TV Samsung", "Tv 50 pulgadas UHD", new BigDecimal(150000), 10, Category.TECH);
		verify(productPersistencePort, times(1)).save(productoSinId);
	}

}