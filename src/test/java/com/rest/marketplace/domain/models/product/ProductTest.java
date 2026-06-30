package com.rest.marketplace.domain.models.product;

import com.rest.marketplace.domain.enums.product.Category;
import com.rest.marketplace.utilities.TestData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void debeCrearProductoConBuilder() {
        Product product = Product.builder()
                .id(1L)
                .title("TV Samsung")
                .description("Tv 50 pulgadas UHD")
                .price(new BigDecimal("150000"))
                .stock(10)
                .category(Category.TECH)
                .build();

        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getTitle()).isEqualTo("TV Samsung");
        assertThat(product.getDescription()).isEqualTo("Tv 50 pulgadas UHD");
        assertThat(product.getPrice()).isEqualByComparingTo(new BigDecimal("150000"));
        assertThat(product.getStock()).isEqualTo(10);
        assertThat(product.getCategory()).isEqualTo(Category.TECH);
    }

    @Test
    void debeCrearProductoConConstructorVacio() {
        Product product = new Product();

        assertThat(product.getId()).isNull();
        assertThat(product.getTitle()).isNull();
        assertThat(product.getDescription()).isNull();
        assertThat(product.getPrice()).isNull();
        assertThat(product.getStock()).isNull();
        assertThat(product.getCategory()).isNull();
    }

    @Test
    void debePermitirModificarCamposConSetter() {
        Product product = new Product();
        product.setId(2L);
        product.setTitle("Heladera");
        product.setDescription("Heladera con freezer");
        product.setPrice(new BigDecimal("80000"));
        product.setStock(5);
        product.setCategory(Category.HOME);

        assertThat(product.getId()).isEqualTo(2L);
        assertThat(product.getTitle()).isEqualTo("Heladera");
        assertThat(product.getDescription()).isEqualTo("Heladera con freezer");
        assertThat(product.getPrice()).isEqualByComparingTo(new BigDecimal("80000"));
        assertThat(product.getStock()).isEqualTo(5);
        assertThat(product.getCategory()).isEqualTo(Category.HOME);
    }

    @Test
    void dosProductosConMismosValoresDebenSerIguales() {
        Product p1 = TestData.productoDominio();
        Product p2 = TestData.productoDominio();

        assertThat(p1).isEqualTo(p2);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }

    @Test
    void dosProductosConDistintoIdDebenSerDiferentes() {
        Product p1 = TestData.productoDominio();
        Product p2 = Product.builder()
                .id(2L)
                .title("TV Samsung")
                .description("Tv 50 pulgadas UHD")
                .price(new BigDecimal("150000"))
                .stock(10)
                .category(Category.TECH)
                .build();

        assertThat(p1).isNotEqualTo(p2);
    }

    @Test
    void debeSerSerializable() {
        assertThat(Product.class.getInterfaces())
                .contains(java.io.Serializable.class);
    }
}


