package com.rest.marketplace.infrastructure.gateways.clients.exchangerate.adapter;

import com.rest.marketplace.infrastructure.gateways.clients.exchangerate.ExchangeRateClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateAdapterTest {

    @Mock
    private ExchangeRateClient exchangeRateClient;

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private ExchangeRateAdapter exchangeRateAdapter;

    @BeforeEach
    void setUp() {
        // Inyectamos el @Value manualmente ya que Mockito no lo hace solo
        ReflectionTestUtils.setField(exchangeRateAdapter, "apiKey", "test-api-key");
    }

    // -------------------------------------------------------------------------
    // Tests del fallbackGetUsdRate
    // -------------------------------------------------------------------------

    @Test
    void fallback_debeRetornarValorCacheado_cuandoHayValorEnCache() {
        // ARRANGE: simulamos que hay un valor cacheado disponible
        BigDecimal cachedRate = new BigDecimal("0.00095");

        Cache mockCache = mock(Cache.class);
        when(cacheManager.getCache("exchangeRate")).thenReturn(mockCache);
        when(mockCache.get("ARS_USD", BigDecimal.class)).thenReturn(cachedRate);

        RuntimeException ex = new RuntimeException("Connection refused");

        // ACT: llamamos directamente al método de fallback
        BigDecimal result = exchangeRateAdapter.fallbackGetUsdRate(ex);

        // ASSERT
        assertThat(result).isEqualByComparingTo(cachedRate);
        verify(cacheManager).getCache("exchangeRate");
        verify(mockCache).get("ARS_USD", BigDecimal.class);
    }

    @Test
    void fallback_debeRetornarValorPorDefecto_cuandoNoHayCache() {
        // ARRANGE: la cache existe pero no tiene el valor
        Cache mockCache = mock(Cache.class);
        when(cacheManager.getCache("exchangeRate")).thenReturn(mockCache);
        when(mockCache.get("ARS_USD", BigDecimal.class)).thenReturn(null);

        RuntimeException ex = new RuntimeException("Timeout");

        // ACT
        BigDecimal result = exchangeRateAdapter.fallbackGetUsdRate(ex);

        // ASSERT: debe retornar el valor hardcoded por defecto
        assertThat(result).isEqualByComparingTo(new BigDecimal("0.00089"));
    }

    @Test
    void fallback_debeRetornarValorPorDefecto_cuandoCacheEsNull() {
        // ARRANGE: el CacheManager no encuentra la cache por nombre
        when(cacheManager.getCache("exchangeRate")).thenReturn(null);

        RuntimeException ex = new RuntimeException("Service unavailable");

        // ACT
        BigDecimal result = exchangeRateAdapter.fallbackGetUsdRate(ex);

        // ASSERT
        assertThat(result).isEqualByComparingTo(new BigDecimal("0.00089"));
    }

    @Test
    void fallback_debeRetornarValorPorDefecto_cuandoOcurreCircuitBreakerOpenException() {
        // ARRANGE: simulamos la excepción que lanza Resilience4j cuando el CB está abierto
        when(cacheManager.getCache("exchangeRate")).thenReturn(null);

        io.github.resilience4j.circuitbreaker.CallNotPermittedException ex =
                io.github.resilience4j.circuitbreaker.CallNotPermittedException
                        .createCallNotPermittedException(
                                io.github.resilience4j.circuitbreaker.CircuitBreaker.ofDefaults("exchangeRate")
                        );

        // ACT
        BigDecimal result = exchangeRateAdapter.fallbackGetUsdRate(ex);

        // ASSERT
        assertThat(result).isEqualByComparingTo(new BigDecimal("0.00089"));
    }
}

