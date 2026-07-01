package com.rest.marketplace.infrastructure.gateways.messaging.adapter;

import com.rest.marketplace.infrastructure.gateways.messaging.entity.MessagingConfigEntity;
import com.rest.marketplace.infrastructure.gateways.messaging.repository.MessagingConfigRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessagingConfigAdapterTest {

    @Mock
    private MessagingConfigRepository messagingConfigRepository;

    @InjectMocks
    private MessagingConfigAdapter messagingConfigAdapter;

    @Test
    void debeRetornarProviderActivoCuandoExisteConfiguracion() {
        MessagingConfigEntity entity = MessagingConfigEntity.builder()
                .id(1L)
                .provider("SQS")
                .active(true)
                .build();

        when(messagingConfigRepository.findActiveConfig()).thenReturn(Optional.of(entity));

        String provider = messagingConfigAdapter.findActiveProvider();

        assertThat(provider).isEqualTo("SQS");
    }

    @Test
    void debeRetornarRabbitPorDefectoCuandoNoExisteConfiguracion() {
        when(messagingConfigRepository.findActiveConfig()).thenReturn(Optional.empty());

        String provider = messagingConfigAdapter.findActiveProvider();

        assertThat(provider).isEqualTo("RABBIT");
    }

    @Test
    void debeRetornarProviderRabbitCuandoConfiguracionEsRabbit() {
        MessagingConfigEntity entity = MessagingConfigEntity.builder()
                .id(2L)
                .provider("RABBIT")
                .active(true)
                .build();

        when(messagingConfigRepository.findActiveConfig()).thenReturn(Optional.of(entity));

        String provider = messagingConfigAdapter.findActiveProvider();

        assertThat(provider).isEqualTo("RABBIT");
    }
}

