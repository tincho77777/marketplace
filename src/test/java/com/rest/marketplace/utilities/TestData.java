package com.rest.marketplace.utilities;

import com.rest.marketplace.domain.enums.product.Category;
import com.rest.marketplace.domain.models.common.PaginationRequest;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.infrastructure.rest.common.response.PageResponse;
import com.rest.marketplace.infrastructure.rest.product.request.create.CreateProductRequest;

import java.math.BigDecimal;
import java.util.List;

public class TestData {

	// IDs
	public static final Long PRODUCT_ID = 1L;
	public static final Long PRODUCT_ID_INEXISTENTE = 99L;

	// PRODUCTO DOMINIO
	public static Product productoDominio() {
		return Product.builder()
				.id(PRODUCT_ID)
				.title("TV Samsung")
				.description("Tv 50 pulgadas UHD")
				.price(new BigDecimal("150000"))
				.stock(10)
				.category(Category.TECH)
				.build();
	}

	// PRODUCTO DOMINIO SIN ID (para crear)
	public static Product productoDominioSinId() {
		return Product.builder()
				.title("TV Samsung")
				.description("Tv 50 pulgadas UHD")
				.price(new BigDecimal("150000"))
				.stock(10)
				.category(Category.TECH)
				.build();
	}

	// REQUEST DE CREACION
	public static CreateProductRequest createProductRequest() {
		return new CreateProductRequest(
				"TV Samsung",
				"Tv 50 pulgadas UHD",
				new BigDecimal("150000"),
				10,
				"tecnologia"
		);
	}

	// REQUEST INVALIDO (para testear validaciones)
	public static CreateProductRequest createProductRequestInvalido() {
		return new CreateProductRequest(
				"",
				"",
				null,
				10,
				"tecnologia"
		);
	}

	// PAGINATION REQUEST
	public static PaginationRequest paginationRequest() {
		return new PaginationRequest(0, 10, "id", "ASC");
	}

	// PAGINATION REQUEST Error Sort
	public static PaginationRequest paginationRequestErrorSortField() {
		return new PaginationRequest(0, 10, "error", "ASC");
	}

	// PAGINATION REQUEST Error Sort
	public static PaginationRequest paginationRequestErrorSortDirection() {
		return new PaginationRequest(0, 10, "id", "error");
	}

	// PAGE RESPONSE
	public static PageResponse<Product> pageResponseProducto() {
		return PageResponse.<Product>builder()
				.content(List.of(productoDominio()))
				.page(0)
				.size(10)
				.totalElements(1L)
				.totalPages(1)
				.build();
	}

	// PAGE RESPONSE VACIA
	public static PageResponse<Product> pageResponseProductoEmpty() {
		return PageResponse.<Product>builder()
				.content(List.of())
				.page(0)
				.size(10)
				.totalElements(0L)
				.totalPages(0)
				.build();
	}

}
