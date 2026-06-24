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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProductServiceTest {

	//@Mock crea una implementación falsa de esa clase o interfaz. No ejecuta código real, todos sus métodos devuelven valores vacíos por default (null, 0, Optional.empty(), etc.) a menos que vos le digas qué devolver con when().thenReturn().
	@Mock
	private ProductPersistencePort productPersistencePort;

	//@InjectMocks crea una instancia real de esa clase, y le inyecta automáticamente todos los @Mock que encuentre en la clase de test que coincidan con sus dependencias.
	@InjectMocks
	private GetProductService getProductService;

	// No devuelve nada (void) porque los assertions se encargan de verificar, no el valor de retorno.
	@Test
	void debeRetornarProductoCuandoExisteElId(){

		// ARRANGE
		Long id = TestData.PRODUCT_ID;
		Product productEsperado = TestData.productoDominio();

		when(productPersistencePort.findById(id))
				.thenReturn(Optional.of(productEsperado));

		// ACT
		Product resultado = getProductService.findById(id);

		// ASSERT
		assertThat(resultado).isNotNull();
		assertThat(resultado.getId()).isEqualTo(id);
		verify(productPersistencePort, times(1)).findById(id);
	}

	@Test
	void debeLanzarNotFoundExceptionCuandoNOExiste(){

		Long id = TestData.PRODUCT_ID_INEXISTENTE;

		when(productPersistencePort.findById(id)).thenReturn(Optional.empty());

		//assertThrows espera que el código dentro del lambda lance exactamente esa excepción. Si la lanza, el test pasa. Si no la lanza, o lanza una diferente, el test falla. El lambda () -> es necesario porque si escribieras getProductService.findById(id) directamente, la excepción se lanzaría antes de que assertThrows pueda capturarla.
		assertThrows(ProductNotFoundException.class, () -> getProductService.findById(id));
		verify(productPersistencePort, times(1)).findById(id);
	}
}