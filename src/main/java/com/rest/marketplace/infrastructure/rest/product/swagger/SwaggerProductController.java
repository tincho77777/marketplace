package com.rest.marketplace.infrastructure.rest.product.swagger;

import com.rest.marketplace.infrastructure.rest.advice.response.ErrorResponse;
import com.rest.marketplace.infrastructure.rest.common.response.PageResponse;
import com.rest.marketplace.infrastructure.rest.product.request.create.CreateProductRequest;
import com.rest.marketplace.infrastructure.rest.product.request.update.UpdateProductRequest;
import com.rest.marketplace.infrastructure.rest.product.response.ProductResponse;
import com.rest.marketplace.infrastructure.utilities.httpcodes.HttpCodes;
import com.rest.marketplace.infrastructure.utilities.mensajes.Mensajes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@Tag(name = "Productos Resource", description = "Operaciones relacionadas con productos")
public interface SwaggerProductController {

	@Operation(summary = "Obtener producto por id", description = "Obtiene un producto a partir de su identificador")
	@ApiResponse(
			responseCode = HttpCodes.OK,
			description = Mensajes.REQUEST_OK
	)
	@ApiResponse(
			responseCode = HttpCodes.NOT_FOUND,
			description = Mensajes.RECURSO_NO_ENCONTRADO,
			content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class)
			)
	)
	@ApiResponse(
			responseCode = HttpCodes.INTERNAL_SERVER_ERROR,
			description = Mensajes.ERROR_INTERNO_SERVIDOR,
			content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class)
			)
	)
	ProductResponse getProduct(@Parameter(description = "Identificador del producto", example = "1", required = true) Long idProducto);


	@Operation(summary = "Alta de producto", description = "Permite registrar un nuevo producto en el marketplace")
	@ApiResponse(
			responseCode = HttpCodes.CREATED,
			description = Mensajes.REQUEST_CREADO
	)
	@ApiResponse(
			responseCode = HttpCodes.BAD_REQUEST,
			description = Mensajes.REQUEST_INVALIDO,
			content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class)
			)
	)
	@ApiResponse(
			responseCode = HttpCodes.INTERNAL_SERVER_ERROR,
			description = Mensajes.ERROR_INTERNO_SERVIDOR,
			content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class)
			)
	)
	ProductResponse createProduct(
			@Valid
			@RequestBody(description = "Datos necesarios para crear un producto", required = true)
			CreateProductRequest request
	);

	@Operation(summary = "Obtener productos con paginado", description = "Obtiene todos los productos con posibilidad de paginar")
	@ApiResponse(
			responseCode = HttpCodes.OK,
			description = Mensajes.REQUEST_OK
	)
	@ApiResponse(
			responseCode = HttpCodes.NOT_FOUND,
			description = Mensajes.RECURSO_NO_ENCONTRADO,
			content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class)
			)
	)
	@ApiResponse(
			responseCode = HttpCodes.INTERNAL_SERVER_ERROR,
			description = Mensajes.ERROR_INTERNO_SERVIDOR,
			content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class)
			)
	)
	PageResponse<ProductResponse> getProducts(@Parameter(description = "Pagina a visualizar", example = "1", required = true)
	                                          @Min(value = 0, message = "La página no puede ser negativa")
	                                          Integer page,
	                                          @Parameter(description = "Cantidad de elementos por pagina", example = "2", required = true)
	                                          @Min(value = 1, message = "El tamaño debe ser mayor a 0")
	                                          Integer size,
	                                          @Parameter(description = "Parametro por el cual ordenar la consulta", example = "id", required = true)
	                                          String sort,
	                                          @Parameter(description = "Orden ASC o DESC para ordenar", example = "ASC", required = true)
	                                          String direction
	);

	@Operation(summary = "Actualizar producto", description = "Actualizar un producto a partir de su identificador")
	@ApiResponse(
			responseCode = HttpCodes.OK,
			description = Mensajes.REQUEST_OK
	)
	@ApiResponse(
			responseCode = HttpCodes.BAD_REQUEST,
			description = Mensajes.REQUEST_INVALIDO,
			content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class)
			)
	)
	@ApiResponse(
			responseCode = HttpCodes.NOT_FOUND,
			description = Mensajes.RECURSO_NO_ENCONTRADO,
			content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class)
			)
	)
	@ApiResponse(
			responseCode = HttpCodes.INTERNAL_SERVER_ERROR,
			description = Mensajes.ERROR_INTERNO_SERVIDOR,
			content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class)
			)
	)
	ProductResponse updateProduct(@Parameter(description = "Identificador del producto", example = "1", required = true)
	                              Long idProducto,
	                              @Valid
	                              @RequestBody(description = "Datos necesarios para crear un producto", required = true)
	                              UpdateProductRequest request
	);


	@Operation(summary = "Eliminar producto", description = "Eliminar un producto a partir de su identificador")
	@ApiResponse(
			responseCode = HttpCodes.NO_CONTENT,
			description = Mensajes.NO_CONTENT
	)
	@ApiResponse(
			responseCode = HttpCodes.BAD_REQUEST,
			description = Mensajes.REQUEST_INVALIDO,
			content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class)
			)
	)
	@ApiResponse(
			responseCode = HttpCodes.NOT_FOUND,
			description = Mensajes.RECURSO_NO_ENCONTRADO,
			content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class)
			)
	)
	@ApiResponse(
			responseCode = HttpCodes.INTERNAL_SERVER_ERROR,
			description = Mensajes.ERROR_INTERNO_SERVIDOR,
			content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = ErrorResponse.class)
			)
	)
	void deleteProduct(@Parameter(description = "Identificador del producto", example = "1", required = true)
	                   Long idProducto
	);
}
