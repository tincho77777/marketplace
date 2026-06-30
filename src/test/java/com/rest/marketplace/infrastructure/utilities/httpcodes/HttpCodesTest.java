package com.rest.marketplace.infrastructure.utilities.httpcodes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpCodesTest {

    @Test
    void debeDefinirCodigoOkComo200() {
        assertThat(HttpCodes.OK).isEqualTo("200");
    }

    @Test
    void debeDefinirCodigoCreatedComo201() {
        assertThat(HttpCodes.CREATED).isEqualTo("201");
    }

    @Test
    void debeDefinirCodigoNoContentComo204() {
        assertThat(HttpCodes.NO_CONTENT).isEqualTo("204");
    }

    @Test
    void debeDefinirCodigoBadRequestComo400() {
        assertThat(HttpCodes.BAD_REQUEST).isEqualTo("400");
    }

    @Test
    void debeDefinirCodigoNotFoundComo404() {
        assertThat(HttpCodes.NOT_FOUND).isEqualTo("404");
    }

    @Test
    void debeDefinirCodigoInternalServerErrorComo500() {
        assertThat(HttpCodes.INTERNAL_SERVER_ERROR).isEqualTo("500");
    }
}

