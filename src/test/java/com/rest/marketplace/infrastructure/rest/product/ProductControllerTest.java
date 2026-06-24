package com.rest.marketplace.infrastructure.rest.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.marketplace.application.usecases.product.*;
import com.rest.marketplace.domain.exceptions.BadRequestException;
import com.rest.marketplace.domain.exceptions.ProductNotFoundException;
import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.infrastructure.rest.product.request.create.CreateProductRequest;
import com.rest.marketplace.utilities.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
		when(getProductUc.findById(id)).thenReturn(producto);

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

		when(getProductUc.findById(id)).thenThrow(new ProductNotFoundException(id));

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
	void debeRetornarTodosLosProductosConParametrosPorDefecto(){

	}

}