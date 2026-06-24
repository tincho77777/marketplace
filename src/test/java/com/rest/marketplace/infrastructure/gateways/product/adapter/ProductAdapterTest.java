package com.rest.marketplace.infrastructure.gateways.product.adapter;

import com.rest.marketplace.domain.enums.product.ProductSortField;
import com.rest.marketplace.domain.enums.product.SortDirection;
import com.rest.marketplace.domain.models.common.PaginationRequest;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.infrastructure.gateways.product.entity.ProductEntity;
import com.rest.marketplace.infrastructure.gateways.product.repository.ProductRepository;
import com.rest.marketplace.infrastructure.rest.common.response.PageResponse;
import com.rest.marketplace.utilities.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductAdapterTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductAdapter productAdapter;

	@Test
	void debeRetornarProductoCuandoExistePorId() {
		Long id = TestData.PRODUCT_ID;
		ProductEntity entity = TestData.productoEntity();

		when(productRepository.findById(id)).thenReturn(Optional.of(entity));

		Optional<Product> result = productAdapter.findById(id);

		assertThat(result).isPresent();
		assertThat(result.get().getId()).isEqualTo(entity.getId());
		assertThat(result.get().getTitle()).isEqualTo(entity.getTitle());
		assertThat(result.get().getDescription()).isEqualTo(entity.getDescription());
		assertThat(result.get().getPrice()).isEqualByComparingTo(entity.getPrice());
		assertThat(result.get().getStock()).isEqualTo(entity.getStock());
		assertThat(result.get().getCategory()).isEqualTo(entity.getCategory());

		verify(productRepository).findById(id);
	}

	@Test
	void debeRetornarEmptyCuandoNoExistePorId() {
		Long id = TestData.PRODUCT_ID_INEXISTENTE;

		when(productRepository.findById(id)).thenReturn(Optional.empty());

		Optional<Product> result = productAdapter.findById(id);

		assertThat(result).isEmpty();
		verify(productRepository).findById(id);
	}

	@Test
	void debeGuardarYRetornarProductoMapeado() {
		Product product = TestData.productoDominioSinId();
		ProductEntity entityGuardada = TestData.productoEntity();

		when(productRepository.save(any(ProductEntity.class))).thenReturn(entityGuardada);

		Product result = productAdapter.save(product);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(entityGuardada.getId());
		assertThat(result.getTitle()).isEqualTo(entityGuardada.getTitle());
		assertThat(result.getDescription()).isEqualTo(entityGuardada.getDescription());
		assertThat(result.getPrice()).isEqualByComparingTo(entityGuardada.getPrice());
		assertThat(result.getStock()).isEqualTo(entityGuardada.getStock());
		assertThat(result.getCategory()).isEqualTo(entityGuardada.getCategory());

		verify(productRepository).save(any(ProductEntity.class));
	}

	@Test
	void debeRetornarProductosPaginadosConSortAsc() {
		PaginationRequest request = TestData.paginationRequest();
		ProductEntity entity = TestData.productoEntity();

		Page<ProductEntity> page = new PageImpl<>(
				List.of(entity),
				PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")),
				1
		);

		when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

		PageResponse<Product> result = productAdapter.findAll(
				request,
				ProductSortField.ID,
				SortDirection.ASC
		);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().getFirst().getId()).isEqualTo(TestData.PRODUCT_ID);
		assertThat(result.getPage()).isEqualTo(0);
		assertThat(result.getSize()).isEqualTo(10);
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getTotalPages()).isEqualTo(1);

		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		verify(productRepository).findAll(pageableCaptor.capture());

		Pageable pageable = pageableCaptor.getValue();
		assertThat(pageable.getPageNumber()).isEqualTo(0);
		assertThat(pageable.getPageSize()).isEqualTo(10);
		assertThat(pageable.getSort().getOrderFor("id")).isNotNull();
		assertThat(pageable.getSort().getOrderFor("id").getDirection()).isEqualTo(Sort.Direction.ASC);
	}

	@Test
	void debeRetornarProductosPaginadosConSortDesc() {
		PaginationRequest request = TestData.paginationRequest();
		ProductEntity entity = TestData.productoEntity();

		Page<ProductEntity> page = new PageImpl<>(
				List.of(entity),
				PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "price")),
				1
		);

		when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

		PageResponse<Product> result = productAdapter.findAll(
				request,
				ProductSortField.PRICE,
				SortDirection.DESC
		);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().getFirst().getId()).isEqualTo(TestData.PRODUCT_ID);

		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		verify(productRepository).findAll(pageableCaptor.capture());

		Pageable pageable = pageableCaptor.getValue();
		assertThat(pageable.getPageNumber()).isEqualTo(0);
		assertThat(pageable.getPageSize()).isEqualTo(10);
		assertThat(pageable.getSort().getOrderFor("price")).isNotNull();
		assertThat(pageable.getSort().getOrderFor("price").getDirection()).isEqualTo(Sort.Direction.DESC);
	}

	@Test
	void debeEliminarProductoPorId() {
		Long id = TestData.PRODUCT_ID;

		productAdapter.deleteById(id);

		verify(productRepository).deleteById(id);
	}
}