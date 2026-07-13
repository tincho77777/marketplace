package com.rest.marketplace.infrastructure.gateways.clients.fakestore.adapter;

import com.rest.marketplace.domain.enums.product.Category;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.infrastructure.gateways.clients.fakestore.FakeStoreClient;
import com.rest.marketplace.infrastructure.gateways.clients.fakestore.model.FakeStoreProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FakeStoreAdapterTest {

	@Mock
	private FakeStoreClient fakeStoreClient;

	@InjectMocks
	private FakeStoreAdapter fakeStoreAdapter;

	private FakeStoreProductDto buildDto(String title, String description, BigDecimal price, String category) {
		try {
			FakeStoreProductDto dto = new FakeStoreProductDto();
			setField(dto, "title", title);
			setField(dto, "description", description);
			setField(dto, "price", price);
			setField(dto, "category", category);
			return dto;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void setField(Object target, String fieldName, Object value) throws Exception {
		var field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	@Test
	void debeImportarProductosYMapearlosDesdeFakeStore() {
		FakeStoreProductDto dto = buildDto("Laptop", "Laptop gamer", new BigDecimal("999.99"), "electronics");
		when(fakeStoreClient.getProducts()).thenReturn(List.of(dto));

		List<Product> resultado = fakeStoreAdapter.importProducts();

		assertThat(resultado).hasSize(1);
		Product product = resultado.get(0);
		assertThat(product.getTitle()).isEqualTo("Laptop");
		assertThat(product.getDescription()).isEqualTo("Laptop gamer");
		assertThat(product.getPrice()).isEqualByComparingTo(new BigDecimal("999.99"));
		assertThat(product.getStock()).isEqualTo(10);
		assertThat(product.getCategory()).isEqualTo(Category.TECH);
		verify(fakeStoreClient, times(1)).getProducts();
	}

	@Test
	void debeMapearCategoriaElectronicsATech() {
		FakeStoreProductDto dto = buildDto("Phone", "Smartphone", new BigDecimal("500.00"), "electronics");
		when(fakeStoreClient.getProducts()).thenReturn(List.of(dto));

		List<Product> resultado = fakeStoreAdapter.importProducts();

		assertThat(resultado.get(0).getCategory()).isEqualTo(Category.TECH);
	}

	@Test
	void debeMapearCategoriaJeweleryAHome() {
		FakeStoreProductDto dto = buildDto("Ring", "Gold ring", new BigDecimal("200.00"), "jewelery");
		when(fakeStoreClient.getProducts()).thenReturn(List.of(dto));

		List<Product> resultado = fakeStoreAdapter.importProducts();

		assertThat(resultado.get(0).getCategory()).isEqualTo(Category.HOME);
	}

	@Test
	void debeMapearCategoriaWomensClothingAHome() {
		FakeStoreProductDto dto = buildDto("Dress", "Summer dress", new BigDecimal("50.00"), "women's clothing");
		when(fakeStoreClient.getProducts()).thenReturn(List.of(dto));

		List<Product> resultado = fakeStoreAdapter.importProducts();

		assertThat(resultado.get(0).getCategory()).isEqualTo(Category.HOME);
	}

	@Test
	void debeMapearCategoriaMensClothingAHome() {
		FakeStoreProductDto dto = buildDto("Shirt", "Casual shirt", new BigDecimal("30.00"), "men's clothing");
		when(fakeStoreClient.getProducts()).thenReturn(List.of(dto));

		List<Product> resultado = fakeStoreAdapter.importProducts();

		assertThat(resultado.get(0).getCategory()).isEqualTo(Category.HOME);
	}

	@Test
	void debeMapearCategoriaDesconocidaATechPorDefecto() {
		FakeStoreProductDto dto = buildDto("Unknown", "Unknown item", new BigDecimal("10.00"), "unknown_category");
		when(fakeStoreClient.getProducts()).thenReturn(List.of(dto));

		List<Product> resultado = fakeStoreAdapter.importProducts();

		assertThat(resultado.get(0).getCategory()).isEqualTo(Category.TECH);
	}

	@Test
	void debeRetornarListaVaciaCuandoFakeStoreNoRetornaProductos() {
		when(fakeStoreClient.getProducts()).thenReturn(List.of());

		List<Product> resultado = fakeStoreAdapter.importProducts();

		assertThat(resultado).isEmpty();
		verify(fakeStoreClient, times(1)).getProducts();
	}

	@Test
	void debeAsignarStockPorDefectoDe10AcadaProducto() {
		FakeStoreProductDto dto1 = buildDto("TV", "Smart TV", new BigDecimal("800.00"), "electronics");
		FakeStoreProductDto dto2 = buildDto("Watch", "Gold watch", new BigDecimal("300.00"), "jewelery");
		when(fakeStoreClient.getProducts()).thenReturn(List.of(dto1, dto2));

		List<Product> resultado = fakeStoreAdapter.importProducts();

		assertThat(resultado).hasSize(2);
		assertThat(resultado).allSatisfy(p -> assertThat(p.getStock()).isEqualTo(10));
	}
}

