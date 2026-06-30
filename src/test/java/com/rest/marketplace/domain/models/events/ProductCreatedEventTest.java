package com.rest.marketplace.domain.models.events;

import com.rest.marketplace.domain.enums.product.Category;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProductCreatedEventTest {

    @Test
    void debeCrearEventoConBuilder() {
        LocalDateTime now = LocalDateTime.now();

        ProductCreatedEvent event = ProductCreatedEvent.builder()
                .id(1L)
                .title("TV Samsung")
                .price(new BigDecimal("150000"))
                .category(Category.TECH)
                .createdAt(now)
                .build();

        assertThat(event.getId()).isEqualTo(1L);
        assertThat(event.getTitle()).isEqualTo("TV Samsung");
        assertThat(event.getPrice()).isEqualByComparingTo(new BigDecimal("150000"));
        assertThat(event.getCategory()).isEqualTo(Category.TECH);
        assertThat(event.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void debeCrearEventoConConstructorVacio() {
        ProductCreatedEvent event = new ProductCreatedEvent();

        assertThat(event.getId()).isNull();
        assertThat(event.getTitle()).isNull();
        assertThat(event.getPrice()).isNull();
        assertThat(event.getCategory()).isNull();
        assertThat(event.getCreatedAt()).isNull();
    }

    @Test
    void debeCrearEventoConConstructorCompleto() {
        LocalDateTime now = LocalDateTime.now();

        ProductCreatedEvent event = new ProductCreatedEvent(1L, "TV Samsung", new BigDecimal("150000"), Category.TECH, now);

        assertThat(event.getId()).isEqualTo(1L);
        assertThat(event.getTitle()).isEqualTo("TV Samsung");
        assertThat(event.getPrice()).isEqualByComparingTo(new BigDecimal("150000"));
        assertThat(event.getCategory()).isEqualTo(Category.TECH);
        assertThat(event.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void debeSerSerializable() {
        assertThat(ProductCreatedEvent.class.getInterfaces())
                .contains(java.io.Serializable.class);
    }
}

