package com.rest.marketplace.infrastructure.configuration.caffeine;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineConfig {

	//La idea de esta config es utilizar los dos caches disponibles, redis y caffeine, la idea de la clase es que primero vaya a caffeine por defecto ya que es mas rapido
	//porq esta en memoria, y si falla va a redis, lo dejo desactivado porque me interesa ver mas redis
//	@Bean
//	@Primary // Spring usa este como default cuando hay múltiples CacheManagers
//	public CacheManager caffeineCacheManager() {
//		CaffeineCacheManager cacheManager = new CaffeineCacheManager();
//		cacheManager.setCaffeine(
//				Caffeine.newBuilder()
//						.maximumSize(500)   // máximo 500 entradas en memoria
//						.expireAfterWrite(5, TimeUnit.MINUTES)  // expira a los 5 minutos
//						.recordStats()     // habilita estadísticas para métricas
//		);
//
//		return cacheManager;
//	}
}

