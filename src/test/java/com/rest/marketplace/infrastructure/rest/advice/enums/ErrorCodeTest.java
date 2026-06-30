package com.rest.marketplace.infrastructure.rest.advice.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorCodeTest {

    @Test
    void debeContenerTodosLosCodigosDeErrorEsperados() {
        ErrorCode[] values = ErrorCode.values();

        assertThat(values).hasSize(6);
        assertThat(values).containsExactly(
                ErrorCode.PRODUCT_NOT_FOUND,
                ErrorCode.VALIDATION_ERROR,
                ErrorCode.INTERNAL_SERVER_ERROR,
                ErrorCode.INVALID_CATEGORY,
                ErrorCode.BAD_REQUEST,
                ErrorCode.OUTBOX_ERROR
        );
    }

    @Test
    void debeResolverProductNotFoundDesdeNombre() {
        ErrorCode code = ErrorCode.valueOf("PRODUCT_NOT_FOUND");

        assertThat(code).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
    }

    @Test
    void debeResolverValidationErrorDesdeNombre() {
        ErrorCode code = ErrorCode.valueOf("VALIDATION_ERROR");

        assertThat(code).isEqualTo(ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void debeResolverInternalServerErrorDesdeNombre() {
        ErrorCode code = ErrorCode.valueOf("INTERNAL_SERVER_ERROR");

        assertThat(code).isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Test
    void debeResolverInvalidCategoryDesdeNombre() {
        ErrorCode code = ErrorCode.valueOf("INVALID_CATEGORY");

        assertThat(code).isEqualTo(ErrorCode.INVALID_CATEGORY);
    }

    @Test
    void debeResolverBadRequestDesdeNombre() {
        ErrorCode code = ErrorCode.valueOf("BAD_REQUEST");

        assertThat(code).isEqualTo(ErrorCode.BAD_REQUEST);
    }

    @Test
    void debeResolverOutboxErrorDesdeNombre() {
        ErrorCode code = ErrorCode.valueOf("OUTBOX_ERROR");

        assertThat(code).isEqualTo(ErrorCode.OUTBOX_ERROR);
    }
}

