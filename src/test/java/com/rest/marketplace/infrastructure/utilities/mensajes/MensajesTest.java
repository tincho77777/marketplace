package com.rest.marketplace.infrastructure.utilities.mensajes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MensajesTest {

    @Test
    void debeDefinirMensajeRequestOk() {
        assertThat(Mensajes.REQUEST_OK).isNotBlank();
    }

    @Test
    void debeDefinirMensajeRequestCreado() {
        assertThat(Mensajes.REQUEST_CREADO).isNotBlank();
    }

    @Test
    void debeDefinirMensajeNoContent() {
        assertThat(Mensajes.NO_CONTENT).isNotBlank();
    }

    @Test
    void debeDefinirMensajeRequestInvalido() {
        assertThat(Mensajes.REQUEST_INVALIDO).isNotBlank();
    }

    @Test
    void debeDefinirMensajeRecursoNoEncontrado() {
        assertThat(Mensajes.RECURSO_NO_ENCONTRADO).isNotBlank();
    }

    @Test
    void debeDefinirMensajeErrorInternoServidor() {
        assertThat(Mensajes.ERROR_INTERNO_SERVIDOR).isNotBlank();
    }
}

