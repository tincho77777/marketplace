package com.rest.marketplace.infrastructure.rest.product;

import com.rest.marketplace.application.usecases.product.*;
import com.rest.marketplace.domain.models.common.PaginationRequest;
import com.rest.marketplace.infrastructure.configuration.ApiPaths;
import com.rest.marketplace.infrastructure.configuration.ApiVersion;
import com.rest.marketplace.infrastructure.rest.common.response.PageResponse;
import com.rest.marketplace.infrastructure.rest.product.request.create.CreateProductRequest;
import com.rest.marketplace.infrastructure.rest.product.request.create.mapper.CreateProductRequestMapper;
import com.rest.marketplace.infrastructure.rest.product.request.update.UpdateProductRequest;
import com.rest.marketplace.infrastructure.rest.product.request.update.mapper.UpdateProductRequestMapper;
import com.rest.marketplace.infrastructure.rest.product.response.ProductResponse;
import com.rest.marketplace.infrastructure.rest.product.response.mapper.ProductResponseMapper;
import com.rest.marketplace.infrastructure.rest.product.swagger.SwaggerProductController;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.BASE_PATH + ApiVersion.V1 + ApiPaths.PRODUCTS)
@RequiredArgsConstructor
public class ProductController implements SwaggerProductController {

	private final GetProductUc getProductUc;
	private final CreateProductUc createProductUc;
	private final GetProductsUc getProductsUc;
	private final UpdateProductUc updateProductUc;
	private final DeleteProductUc deleteProductUc;

	@GetMapping("/{idProducto}")
	public ProductResponse getProduct(@PathVariable Long idProducto){
		return ProductResponseMapper.toResponse(getProductUc.findById(idProducto));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request){
		return ProductResponseMapper.toResponse(createProductUc.createProduct(CreateProductRequestMapper.toDomain(request)));
	}

	//borrar
	@GetMapping
	public PageResponse<ProductResponse> getProducts(@RequestParam(defaultValue = "0")
	                                                 @Min(value = 0, message = "La página no puede ser negativa")
	                                                 Integer page,
	                                                 @RequestParam(defaultValue = "10")
	                                                 @Min(value = 1, message = "El tamaño debe ser mayor a 0")
	                                                 Integer size,
	                                                 @RequestParam(defaultValue = "id") String sort,
	                                                 @RequestParam(defaultValue = "asc") String direction){

		var result = getProductsUc.getProducts(new PaginationRequest(page, size, sort, direction));
		return PageResponse.<ProductResponse>builder().content(
				result.getContent()
						.stream()
						.map(ProductResponseMapper::toResponse)
						.toList()
				)
				.page(result.getPage())
				.size(result.getSize())
				.totalElements(result.getTotalElements())
				.totalPages(result.getTotalPages())
				.build();
	}

	@PutMapping("/{idProducto}")
	public ProductResponse updateProduct(@PathVariable Long idProducto,
	                                     @Valid @RequestBody UpdateProductRequest request){

		return ProductResponseMapper.toResponse(updateProductUc.updateProduct(
				idProducto,
				UpdateProductRequestMapper.toDomain(request))
		);
	}

	@DeleteMapping("/{idProducto}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProduct(@PathVariable Long idProducto){
		deleteProductUc.deleteProduct(idProducto);
	}
}
