package com.rest.marketplace.application.services;

import com.rest.marketplace.domain.exceptions.ProductNotFoundException;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import com.rest.marketplace.utilities.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductServiceTest {

	@Mock
	private ProductPersistencePort productPersistencePort;

	@InjectMocks
	private UpdateProductService updateProductService;

	@Test
	void debeActualizarCorrectamenteElProductoSiLosDatosSonCorrectos(){

		Long id = TestData.PRODUCT_ID;
		Product productoExistente  = TestData.productoDominio();
		Product productoActualizado  = TestData.productoDominioSinId();

		when(productPersistencePort.findById(id)).thenReturn(Optional.of(productoExistente));
		when(productPersistencePort.save(productoExistente)).thenReturn(productoExistente);

		Product resultado = updateProductService.updateProduct(id, productoActualizado);

		assertThat(resultado.getTitle()).isEqualTo(productoActualizado.getTitle());
		assertThat(resultado.getDescription()).isEqualTo(productoActualizado.getDescription());
		assertThat(resultado.getPrice()).isEqualTo(productoActualizado.getPrice());
		assertThat(resultado.getStock()).isEqualTo(productoActualizado.getStock());
		assertThat(resultado.getCategory()).isEqualTo(productoActualizado.getCategory());
		verify(productPersistencePort, times(1)).findById(id);
		verify(productPersistencePort, times(1)).save(productoExistente);
	}

	@Test
	void debeLanzarProductNotFoundExceptionSiNoExisteElProducto(){
		Long id = TestData.PRODUCT_ID;
		Product productoExistente  = TestData.productoDominio();

		when(productPersistencePort.findById(id)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> updateProductService.updateProduct(id, productoExistente)).isInstanceOf(ProductNotFoundException.class);

		verify(productPersistencePort, times(1)).findById(id);
		verify(productPersistencePort, times(0)).save(productoExistente);
	}

}