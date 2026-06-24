package com.rest.marketplace.application.services;

import com.rest.marketplace.domain.enums.product.ProductSortField;
import com.rest.marketplace.domain.enums.product.SortDirection;
import com.rest.marketplace.domain.exceptions.BadRequestException;
import com.rest.marketplace.domain.models.common.PaginationRequest;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.domain.ports.product.ProductPersistencePort;
import com.rest.marketplace.infrastructure.rest.common.response.PageResponse;
import com.rest.marketplace.utilities.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.rest.marketplace.utilities.TestData.pageResponseProductoEmpty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProductsServiceTest {

	@Mock
	private ProductPersistencePort productPersistencePort;

	@InjectMocks
	private GetProductsService getProductsService;

	@Test
	void debeRetornarProductosPaginadosCuandoParametrosSonValidos(){

		PaginationRequest request = TestData.paginationRequest();
		PageResponse<Product> pageEsperada = TestData.pageResponseProducto();
		ProductSortField productSortField = ProductSortField.from(request.getSort());
		SortDirection sortDirection = SortDirection.from(request.getDirection());

		when(productPersistencePort.findAll(request, productSortField, sortDirection)).thenReturn(pageEsperada);

		PageResponse<Product> resultado = getProductsService.getProducts(request);

		assertThat(resultado.getContent()).hasSize(1);
		assertThat(resultado.getTotalElements()).isEqualTo(1L);
		assertThat(resultado.getPage()).isZero();
		verify(productPersistencePort, times(1)).findAll(request, productSortField, sortDirection);
	}

	@Test
	void debeLanzarBadRequestExceptionCuandoSortFieldEsInvalido(){

		PaginationRequest request = TestData.paginationRequestErrorSortField();

		assertThrows(BadRequestException.class, () -> getProductsService.getProducts(request));

		verifyNoInteractions(productPersistencePort);
	}

	@Test
	void debeLanzarBadRequestExceptionCuandoSortDirectionEsInvalido(){

		PaginationRequest request = TestData.paginationRequestErrorSortDirection();

		assertThrows(BadRequestException.class, () -> getProductsService.getProducts(request));

		verifyNoInteractions(productPersistencePort);
	}

	@Test
	void debeTraerSoloLosDatosDePaginadoSiNoHayProductos(){

		PaginationRequest request = TestData.paginationRequest();
		ProductSortField productSortField = ProductSortField.from(request.getSort());
		SortDirection sortDirection = SortDirection.from(request.getDirection());
		PageResponse<Product> responseEsperada = pageResponseProductoEmpty();

		when(productPersistencePort.findAll(request, productSortField, sortDirection)).thenReturn(responseEsperada);

		PageResponse<Product> responseObtenida = getProductsService.getProducts(request);

		assertThat(responseObtenida.getContent()).isEmpty();
		assertThat(responseObtenida.getTotalElements()).isZero();
		assertThat(responseObtenida.getPage()).isZero();
		verify(productPersistencePort, times(1)).findAll(request, productSortField, sortDirection);
	}

}