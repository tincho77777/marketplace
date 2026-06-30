package com.rest.marketplace.domain.enums.outbox;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OutboxStatusTest {

    @Test
    void debeContenerLosTresEstadosEsperados() {
        OutboxStatus[] values = OutboxStatus.values();

        assertThat(values).hasSize(3);
        assertThat(values).containsExactly(
                OutboxStatus.PENDING,
                OutboxStatus.PROCESSED,
                OutboxStatus.FAILED
        );
    }

    @Test
    void debeResolverPendingDesdeNombre() {
        OutboxStatus status = OutboxStatus.valueOf("PENDING");

        assertThat(status).isEqualTo(OutboxStatus.PENDING);
    }

    @Test
    void debeResolverProcessedDesdeNombre() {
        OutboxStatus status = OutboxStatus.valueOf("PROCESSED");

        assertThat(status).isEqualTo(OutboxStatus.PROCESSED);
    }

    @Test
    void debeResolverFailedDesdeNombre() {
        OutboxStatus status = OutboxStatus.valueOf("FAILED");

        assertThat(status).isEqualTo(OutboxStatus.FAILED);
    }
}

