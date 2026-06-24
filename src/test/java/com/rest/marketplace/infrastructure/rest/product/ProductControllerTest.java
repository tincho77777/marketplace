package com.rest.marketplace.infrastructure.rest.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.marketplace.application.usecases.product.*;
import com.rest.marketplace.domain.exceptions.BadRequestException;
import com.rest.marketplace.domain.exceptions.InvalidCategoryException;
import com.rest.marketplace.domain.exceptions.ProductNotFoundException;
import com.rest.marketplace.domain.models.common.PaginationRequest;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.infrastructure.rest.common.response.PageResponse;
import com.rest.marketplace.infrastructure.rest.product.request.create.CreateProductRequest;
import com.rest.marketplace.infrastructure.rest.product.request.update.UpdateProductRequest;
import com.rest.marketplace.utilities.TestData;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Le dice a Spring que levante solo la capa web, únicamente lo necesario para testear el controller: filtros, serialización JSON, validaciones, el @RestControllerAdvice. No levanta la base de datos, no levanta los services, no levanta los adapters. Es mucho más rápido que levantar el contexto de Spring. El (ProductController.class) le dice específicamente qué controller querés testear, para no levantar todos los controllers de la aplicación.
@WebMvcTest(ProductController.class)
class ProductControllerTest {

	//MockMvc es el objeto que te permite simular llamadas HTTP sin levantar un servidor real.
	@Autowired private MockMvc mockMvc;

	//Es el serializador/deserializador de JSON de Jackson. Lo usás para convertir tus objetos Java a JSON cuando necesitás mandar un body en el request
	@Autowired private ObjectMapper objectMapper;

	//@MockBean es la versión de @Mock pero para el contexto de Spring. La diferencia es importante: @Mock crea un mock puro de Mockito sin involucrar a Spring, mientras que @MockBean crea un mock y lo registra en el contexto de Spring para que cuando Spring intente inyectar la dependencia en el controller, encuentre este mock en lugar de buscar una implementación real. Si usaras @Mock acá, Spring no sabría de su existencia y tiraría error al intentar inyectar los use cases en el controller.
	//Necesitás mockear todos los use cases que el controller inyecta, aunque no los uses en un test específico, porque Spring intenta inyectarlos todos al crear el controller.
	@MockitoBean private GetProductUc getProductUc;
	@MockitoBean private CreateProductUc createProductUc;
	@MockitoBean private GetProductsUc getProductsUc;
	@MockitoBean private UpdateProductUc updateProductUc;
	@MockitoBean private DeleteProductUc deleteProductUc;

	/**
	 * TEST DE ALTA DE PRODUCTOS
	 */
	@Test
	void debeRetornar201CuandoCreateEsValido() throws Exception {

		CreateProductRequest request = TestData.createProductRequest();
		Product productoCreado = TestData.productoDominio();

		when(createProductUc.createProduct(any(Product.class))).thenReturn(productoCreado);

		mockMvc.perform(post("/marketplace/v1/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(TestData.PRODUCT_ID))
				.andExpect(jsonPath("$.title").value("TV Samsung"));
	}

	@Test
	void debeRetornar400CuandoFaltanParametrosObligatorios() throws Exception {

		CreateProductRequest request = TestData.createProductRequestInvalido();

		mockMvc.perform(post("/marketplace/v1/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.detail").value("VALIDATION_ERROR"))
				.andExpect(jsonPath("$.message").value("Se encontraron errores de validación"))
				.andExpect(jsonPath("$.path").value("/marketplace/v1/products"))
				.andExpect(jsonPath("$.timestamp").exists())
				.andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.errors").isNotEmpty());

		verifyNoInteractions(createProductUc);
	}

	@Test
	void debeRetornar400CuandoSortFieldEsInvalido() throws Exception {

		when(getProductsUc.getProducts(any()))
				.thenThrow(new BadRequestException("Campo de ordenamiento invalido: campoIncorrecto"));

		mockMvc.perform(get("/marketplace/v1/products")
						.param("page", "0")
						.param("size", "10")
						.param("sort", "campoIncorrecto")
						.param("direction", "asc"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.detail").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.message").value("Campo de ordenamiento invalido: campoIncorrecto"))
				.andExpect(jsonPath("$.path").value("/marketplace/v1/products"));
	}

	/**
	 * TEST DE CONSULTA DE PRODUCTO POR ID
	 */
	@Test
	void debeRetornarUnProductoCuandoExisteSuId() throws Exception {

		Long id = TestData.PRODUCT_ID;
		Product producto = TestData.productoDominio();
		when(getProductUc.getProductById(id)).thenReturn(producto);

		mockMvc.perform(get("/marketplace/v1/products/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.title").value("TV Samsung"))
				.andExpect(jsonPath("$.description").value("Tv 50 pulgadas UHD"))
				.andExpect(jsonPath("$.price").value(150000))
				.andExpect(jsonPath("$.category").value("tecnologia"));
	}

	@Test
	void debeRetornarProductNotFoundExceptionCuandoNoExisteElId() throws Exception {

		Long id = TestData.PRODUCT_ID_INEXISTENTE;

		when(getProductUc.getProductById(id)).thenThrow(new ProductNotFoundException(id));

		mockMvc.perform(get("/marketplace/v1/products/{id}", id))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value(404))
				.andExpect(jsonPath("$.detail").value("PRODUCT_NOT_FOUND"))
				.andExpect(jsonPath("$.message").value("Producto con id " + id + " no encontrado."));
	}

	/**
	 * TEST DE CONSULTA DE PRODUCTOS POR PAGINADO
	 */
	@Test
	void debeRetornarTodosLosProductosConParametrosPorDefecto() throws Exception {

		PageResponse<Product> response = TestData.pageResponseProducto();

		when(getProductsUc.getProducts(argThat(req ->
				req.getPage().equals(0)
						&& req.getSize().equals(10)
						&& req.getSort().equals("id")
						&& req.getDirection().equals("asc")
		))).thenReturn(response);

		mockMvc.perform(get("/marketplace/v1/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].id").value(TestData.PRODUCT_ID))
				.andExpect(jsonPath("$.content[0].title").value("TV Samsung"))
				.andExpect(jsonPath("$.content[0].description").value("Tv 50 pulgadas UHD"))
				.andExpect(jsonPath("$.content[0].price").value(150000))
				.andExpect(jsonPath("$.content[0].stock").value(10))
				.andExpect(jsonPath("$.content[0].category").value("tecnologia"))
				.andExpect(jsonPath("$.page").value(0))
				.andExpect(jsonPath("$.size").value(10))
				.andExpect(jsonPath("$.totalElements").value(1))
				.andExpect(jsonPath("$.totalPages").value(1));
	}

	@Test
	void debeRetornarProductosConParametrosExplicitamenteInformados() throws Exception {

		PageResponse<Product> response = TestData.pageResponseProducto();

		when(getProductsUc.getProducts(any(PaginationRequest.class))).thenReturn(response);

		mockMvc.perform(get("/marketplace/v1/products")
						.param("page", "1")
						.param("size", "5")
						.param("sort", "price")
						.param("direction", "desc"))
				.andExpect(status().isOk());

		ArgumentCaptor<PaginationRequest> captor = ArgumentCaptor.forClass(PaginationRequest.class);
		verify(getProductsUc).getProducts(captor.capture());

		PaginationRequest request = captor.getValue();
		assertThat(request.getPage()).isEqualTo(1);
		assertThat(request.getSize()).isEqualTo(5);
		assertThat(request.getSort()).isEqualTo("price");
		assertThat(request.getDirection()).isEqualTo("desc");
	}

	@Test
	void debeRetornar200ConListaVaciaCuandoNoHayProductos() throws Exception {

		PageResponse<Product> response = TestData.pageResponseProductoEmpty();

		when(getProductsUc.getProducts(any(PaginationRequest.class))).thenReturn(response);

		mockMvc.perform(get("/marketplace/v1/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content").isEmpty())
				.andExpect(jsonPath("$.totalElements").value(0))
				.andExpect(jsonPath("$.totalPages").value(0));
	}

	@Test
	void debeRetornar400CuandoPageEsNegativa() throws Exception {

		mockMvc.perform(get("/marketplace/v1/products")
						.param("page", "-1"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.errors[0].field").value("page"))
				.andExpect(jsonPath("$.errors[0].message").value("La página no puede ser negativa"));
	}

	@Test
	void debeRetornar400CuandoSizeEsCero() throws Exception {

		mockMvc.perform(get("/marketplace/v1/products")
						.param("size", "0"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.errors[0].field").value("size"));
	}

	@Test
	void debeRetornar400CuandoSortEsInvalido() throws Exception {

		when(getProductsUc.getProducts(any(PaginationRequest.class)))
				.thenThrow(new BadRequestException("Campo de ordeamiento invalido: " + "pepe"));

		mockMvc.perform(get("/marketplace/v1/products")
						.param("sort", "pepe"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detail").value("BAD_REQUEST"));
	}

	@Test
	void debeRetornar400CuandoDirectionEsInvalida() throws Exception {

		when(getProductsUc.getProducts(any(PaginationRequest.class)))
				.thenThrow(new BadRequestException("Dirección inválida: " + "pepe"));

		mockMvc.perform(get("/marketplace/v1/products")
						.param("direction", "pepe"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detail").value("BAD_REQUEST"));
	}

	/**
	 * TEST DE ACTUALIZACION DE PRODUCTOS
	 */
	@Test
	void debeRetornar200CuandoUpdateEsValido() throws Exception {

		UpdateProductRequest request = TestData.updateProductRequest();
		Product productoActualizado = TestData.productoDominio();

		when(updateProductUc.updateProduct(eq(TestData.PRODUCT_ID), any(Product.class))).thenReturn(productoActualizado);

		mockMvc.perform(put("/marketplace/v1/products/{id}", TestData.PRODUCT_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(TestData.PRODUCT_ID))
				.andExpect(jsonPath("$.title").value("TV Samsung"))
				.andExpect(jsonPath("$.description").value("Tv 50 pulgadas UHD"))
				.andExpect(jsonPath("$.price").value(150000))
				.andExpect(jsonPath("$.stock").value(10))
				.andExpect(jsonPath("$.category").value("tecnologia"));

		verify(updateProductUc, times(1)).updateProduct(eq(TestData.PRODUCT_ID), any(Product.class));
	}

	@Test
	void debeRetornar400CuandoUpdateRequestEsInvalido() throws Exception {

		UpdateProductRequest request = TestData.updateProductRequestInvalido();

		mockMvc.perform(put("/marketplace/v1/products/{id}", TestData.PRODUCT_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.errors[0].field").exists())
				.andExpect(jsonPath("$.errors[0].message").exists());

		verify(updateProductUc, never()).updateProduct(anyLong(), any(Product.class));
	}

	@Test
	void debeRetornar404CuandoProductoAActualizarNoExiste() throws Exception {

		UpdateProductRequest request = TestData.updateProductRequest();

		when(updateProductUc.updateProduct(eq(TestData.PRODUCT_ID_INEXISTENTE), any(Product.class))).thenThrow(new ProductNotFoundException(TestData.PRODUCT_ID_INEXISTENTE));

		mockMvc.perform(put("/marketplace/v1/products/{id}", TestData.PRODUCT_ID_INEXISTENTE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value(404));

		verify(updateProductUc, times(1)).updateProduct(eq(TestData.PRODUCT_ID_INEXISTENTE), any(Product.class));
	}

	@Test
	void debeRetornar400CuandoCategoriaDelUpdateEsInvalida() throws Exception {

		UpdateProductRequest request = TestData.updateProductRequestConCategoriaInvalida();

		when(updateProductUc.updateProduct(eq(TestData.PRODUCT_ID), any(Product.class))).thenThrow(new InvalidCategoryException("TECH"));

		mockMvc.perform(put("/marketplace/v1/products/{id}", TestData.PRODUCT_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.detail").value("VALIDATION_ERROR"));
	}

	/**
	 * TEST DE ACTUALIZACION DE PRODUCTOS
	 */
	@Test
	void debeRetornar204CuandoDeleteEsValido() throws Exception {

		mockMvc.perform(delete("/marketplace/v1/products/{id}", TestData.PRODUCT_ID))
				.andExpect(status().isNoContent());

		verify(deleteProductUc, times(1)).deleteProduct(TestData.PRODUCT_ID);
	}

	@Test
	void debeRetornar404CuandoProductoAEliminarNoExiste() throws Exception {

		doThrow(new ProductNotFoundException(TestData.PRODUCT_ID_INEXISTENTE))
				.when(deleteProductUc).deleteProduct(TestData.PRODUCT_ID_INEXISTENTE);

		mockMvc.perform(delete("/marketplace/v1/products/{id}", TestData.PRODUCT_ID_INEXISTENTE))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value(404));

		verify(deleteProductUc, times(1)).deleteProduct(TestData.PRODUCT_ID_INEXISTENTE);
	}

}