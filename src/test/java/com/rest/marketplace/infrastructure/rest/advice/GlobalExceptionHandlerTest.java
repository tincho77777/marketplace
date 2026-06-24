package com.rest.marketplace.infrastructure.rest.advice;

import com.rest.marketplace.domain.exceptions.BadRequestException;
import com.rest.marketplace.domain.exceptions.InvalidCategoryException;
import com.rest.marketplace.domain.exceptions.ProductNotFoundException;
import com.rest.marketplace.infrastructure.rest.advice.response.ErrorResponse;
import com.rest.marketplace.infrastructure.rest.advice.response.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

	private GlobalExceptionHandler globalExceptionHandler;
	private HttpServletRequest request;

	@BeforeEach
	void setUp() {
		globalExceptionHandler = new GlobalExceptionHandler();
		request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/marketplace/v1/products");
	}

	@Test
	void debeRetornar404CuandoProductoNoExiste() {
		ProductNotFoundException exception = new ProductNotFoundException(1L);

		ResponseEntity<ErrorResponse> response =
				globalExceptionHandler.handleProductNotFound(exception, request);

		assertThat(response.getStatusCode().value()).isEqualTo(404);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().code()).isEqualTo(404);
		assertThat(response.getBody().detail()).isEqualTo("PRODUCT_NOT_FOUND");
		assertThat(response.getBody().message()).isEqualTo(exception.getMessage());
		assertThat(response.getBody().path()).isEqualTo("/marketplace/v1/products");
		assertThat(response.getBody().timestamp()).isNotNull();
	}

	@Test
	void debeRetornar400CuandoCategoriaEsInvalida() {
		InvalidCategoryException exception = new InvalidCategoryException("categoria-rara");

		ResponseEntity<ErrorResponse> response =
				globalExceptionHandler.handleInvalidCategory(exception, request);

		assertThat(response.getStatusCode().value()).isEqualTo(400);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().code()).isEqualTo(400);
		assertThat(response.getBody().detail()).isEqualTo("INVALID_CATEGORY");
		assertThat(response.getBody().message()).isEqualTo(exception.getMessage());
		assertThat(response.getBody().path()).isEqualTo("/marketplace/v1/products");
		assertThat(response.getBody().timestamp()).isNotNull();
	}

	@Test
	void debeRetornar400CuandoHayBadRequest() {
		BadRequestException exception = new BadRequestException("Campo de ordenamiento inválido");

		ResponseEntity<ErrorResponse> response =
				globalExceptionHandler.handleBadRequest(exception, request);

		assertThat(response.getStatusCode().value()).isEqualTo(400);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().code()).isEqualTo(400);
		assertThat(response.getBody().detail()).isEqualTo("BAD_REQUEST");
		assertThat(response.getBody().message()).isEqualTo("Campo de ordenamiento inválido");
		assertThat(response.getBody().path()).isEqualTo("/marketplace/v1/products");
		assertThat(response.getBody().timestamp()).isNotNull();
	}

	@Test
	void debeRetornar400ConErroresDeBodyCuandoMethodArgumentNotValidException() throws Exception {
		Object target = new Object();
		BeanPropertyBindingResult bindingResult =
				new BeanPropertyBindingResult(target, "createProductRequest");

		bindingResult.addError(new FieldError(
				"createProductRequest",
				"title",
				"El título es obligatorio"
		));

		bindingResult.addError(new FieldError(
				"createProductRequest",
				"price",
				"El precio es obligatorio"
		));

		Method method = DummyController.class.getDeclaredMethod("dummyMethod", DummyRequest.class);
		MethodParameter methodParameter = new MethodParameter(method, 0);

		MethodArgumentNotValidException exception =
				new MethodArgumentNotValidException(methodParameter, bindingResult);

		ResponseEntity<ValidationErrorResponse> response =
				globalExceptionHandler.handleArgumentNotValidException(exception, request);

		assertThat(response.getStatusCode().value()).isEqualTo(400);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().code()).isEqualTo(400);
		assertThat(response.getBody().detail()).isEqualTo("VALIDATION_ERROR");
		assertThat(response.getBody().message()).isEqualTo("Se encontraron errores de validación");
		assertThat(response.getBody().path()).isEqualTo("/marketplace/v1/products");
		assertThat(response.getBody().timestamp()).isNotNull();

		assertThat(response.getBody().errors()).hasSize(2);
		assertThat(response.getBody().errors().get(0).field()).isEqualTo("title");
		assertThat(response.getBody().errors().get(0).message()).isEqualTo("El título es obligatorio");
		assertThat(response.getBody().errors().get(1).field()).isEqualTo("price");
		assertThat(response.getBody().errors().get(1).message()).isEqualTo("El precio es obligatorio");
	}

	// clases dummy para construir exceptions de validación
	private static class DummyController {
		public void dummyMethod(DummyRequest request) {}
	}

	private static class DummyRequest {
	}

}