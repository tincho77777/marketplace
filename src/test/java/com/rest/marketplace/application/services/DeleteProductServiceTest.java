package com.rest.marketplace.application.services;

import com.rest.marketplace.domain.exceptions.ProductNotFoundException;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import com.rest.marketplace.utilities.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteProductServiceTest {

	@Mock
	private ProductPersistencePort productPersistencePort;

	@InjectMocks
	private DeleteProductService deleteProductService;

	@Test
	void debeEliminarElProductoCorrectamente(){

		Long id = TestData.PRODUCT_ID;

		when(productPersistencePort.findById(id)).thenReturn(Optional.of(TestData.productoDominio()));

		deleteProductService.deleteProduct(id);

		verify(productPersistencePort, times(1)).findById(id);
		verify(productPersistencePort, times(1)).deleteById(id);
	}

	@Test
	void deleLanzarProductNotFoundExceptionSiElProductoAEliminarNoExiste(){

		Long id = TestData.PRODUCT_ID;

		when(productPersistencePort.findById(id)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> deleteProductService.deleteProduct(id)).isInstanceOf(ProductNotFoundException.class);

		verify(productPersistencePort, times(1)).findById(id);
		verify(productPersistencePort, times(0)).deleteById(id);
	}

}