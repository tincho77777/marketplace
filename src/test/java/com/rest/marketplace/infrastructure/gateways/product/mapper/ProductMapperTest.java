package com.rest.marketplace.infrastructure.gateways.product.mapper;

import com.rest.marketplace.domain.models.product.Product;
import com.rest.marketplace.infrastructure.gateways.product.entity.ProductEntity;
import com.rest.marketplace.utilities.TestData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest {

	@Test
	void debeMapearCorrectamenteDeEntityADomain() {
		ProductEntity entity = ProductEntity.builder()
				.id(TestData.PRODUCT_ID)
				.title("TV Samsung")
				.description("Tv 50 pulgadas UHD")
				.price(TestData.productoDominio().getPrice())
				.stock(10)
				.category(TestData.productoDominio().getCategory())
				.build();

		Product result = ProductMapper.toDomain(entity);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(entity.getId());
		assertThat(result.getTitle()).isEqualTo(entity.getTitle());
		assertThat(result.getDescription()).isEqualTo(entity.getDescription());
		assertThat(result.getPrice()).isEqualByComparingTo(entity.getPrice());
		assertThat(result.getStock()).isEqualTo(entity.getStock());
		assertThat(result.getCategory()).isEqualTo(entity.getCategory());
	}

	@Test
	void debeRetornarNullCuandoEntityEsNull() {
		Product result = ProductMapper.toDomain(null);

		assertThat(result).isNull();
	}

	@Test
	void debeMapearCorrectamenteDeDomainAEntity() {
		Product product = TestData.productoDominio();

		ProductEntity result = ProductMapper.toEntity(product);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(product.getId());
		assertThat(result.getTitle()).isEqualTo(product.getTitle());
		assertThat(result.getDescription()).isEqualTo(product.getDescription());
		assertThat(result.getPrice()).isEqualByComparingTo(product.getPrice());
		assertThat(result.getStock()).isEqualTo(product.getStock());
		assertThat(result.getCategory()).isEqualTo(product.getCategory());
	}

	@Test
	void debeRetornarNullCuandoProductEsNull() {
		ProductEntity result = ProductMapper.toEntity(null);

		assertThat(result).isNull();
	}

}