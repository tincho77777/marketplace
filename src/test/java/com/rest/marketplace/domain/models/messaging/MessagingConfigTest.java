package com.rest.marketplace.domain.models.messaging;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MessagingConfigTest {

    @Test
    void debeCrearMessagingConfigConBuilder() {
        LocalDateTime now = LocalDateTime.now();

        MessagingConfig config = MessagingConfig.builder()
                .id(1L)
                .provider("SQS")
                .active(true)
                .updatedAt(now)
                .build();

        assertThat(config.getId()).isEqualTo(1L);
        assertThat(config.getProvider()).isEqualTo("SQS");
        assertThat(config.getActive()).isTrue();
        assertThat(config.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void debeCrearMessagingConfigConConstructorVacio() {
        MessagingConfig config = new MessagingConfig();

        assertThat(config.getId()).isNull();
        assertThat(config.getProvider()).isNull();
        assertThat(config.getActive()).isNull();
        assertThat(config.getUpdatedAt()).isNull();
    }

    @Test
    void debeCrearMessagingConfigConConstructorCompleto() {
        LocalDateTime now = LocalDateTime.now();

        MessagingConfig config = new MessagingConfig(2L, "RABBIT", false, now);

        assertThat(config.getId()).isEqualTo(2L);
        assertThat(config.getProvider()).isEqualTo("RABBIT");
        assertThat(config.getActive()).isFalse();
        assertThat(config.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void debePermitirCambiarValoresConSetters() {
        MessagingConfig config = new MessagingConfig();

        config.setId(3L);
        config.setProvider("SQS");
        config.setActive(true);

        assertThat(config.getId()).isEqualTo(3L);
        assertThat(config.getProvider()).isEqualTo("SQS");
        assertThat(config.getActive()).isTrue();
    }
}

