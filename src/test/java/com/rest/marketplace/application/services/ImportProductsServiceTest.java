package com.rest.marketplace.application.services;

import com.rest.marketplace.application.services.product.ImportProductsService;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.domain.ports.fakestore.FakeStorePort;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import com.rest.marketplace.utilities.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportProductsServiceTest {

	@Mock
	private FakeStorePort fakeStorePort;

	@Mock
	private ProductPersistencePort productPersistencePort;

	@InjectMocks
	private ImportProductsService importProductsService;

	@Test
	void debeImportarProductosCuandoFakeStoreRetornaResultados() {
		Product producto = TestData.productoDominioSinId();
		Product productoGuardado = TestData.productoDominio();
		List<Product> productosImportados = List.of(producto);

		when(fakeStorePort.importProducts()).thenReturn(productosImportados);
		when(productPersistencePort.save(producto)).thenReturn(productoGuardado);

		int resultado = importProductsService.importFromFakeStore();

		assertThat(resultado).isEqualTo(1);
		verify(fakeStorePort, times(1)).importProducts();
		verify(productPersistencePort, times(1)).save(producto);
	}

	@Test
	void debeRetornarCeroProductosCuandoFakeStoreRetornaListaVacia() {
		when(fakeStorePort.importProducts()).thenReturn(List.of());

		int resultado = importProductsService.importFromFakeStore();

		assertThat(resultado).isZero();
		verify(fakeStorePort, times(1)).importProducts();
		verifyNoInteractions(productPersistencePort);
	}

	@Test
	void debeImportarMultiplesProductosCuandoFakeStoreRetornaVariosResultados() {
		Product producto1 = TestData.productoDominioSinId();
		Product producto2 = TestData.productoDominioSinId();
		List<Product> productosImportados = List.of(producto1, producto2);

		when(fakeStorePort.importProducts()).thenReturn(productosImportados);
		when(productPersistencePort.save(any(Product.class))).thenReturn(TestData.productoDominio());

		int resultado = importProductsService.importFromFakeStore();

		assertThat(resultado).isEqualTo(2);
		verify(fakeStorePort, times(1)).importProducts();
		verify(productPersistencePort, times(2)).save(any(Product.class));
	}
}

