package com.rest.marketplace.domain.models.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaginationRequestTest {

    @Test
    void debeCrearPaginationRequestConTodosLosValores() {
        PaginationRequest request = new PaginationRequest(0, 10, "id", "ASC");

        assertThat(request.getPage()).isZero();
        assertThat(request.getSize()).isEqualTo(10);
        assertThat(request.getSort()).isEqualTo("id");
        assertThat(request.getDirection()).isEqualTo("ASC");
    }

    @Test
    void debeCrearPaginationRequestConPaginaYTamanoDiferente() {
        PaginationRequest request = new PaginationRequest(2, 20, "price", "DESC");

        assertThat(request.getPage()).isEqualTo(2);
        assertThat(request.getSize()).isEqualTo(20);
        assertThat(request.getSort()).isEqualTo("price");
        assertThat(request.getDirection()).isEqualTo("DESC");
    }

    @Test
    void debePermitirSortNulo() {
        PaginationRequest request = new PaginationRequest(0, 5, null, null);

        assertThat(request.getPage()).isZero();
        assertThat(request.getSize()).isEqualTo(5);
        assertThat(request.getSort()).isNull();
        assertThat(request.getDirection()).isNull();
    }
}

