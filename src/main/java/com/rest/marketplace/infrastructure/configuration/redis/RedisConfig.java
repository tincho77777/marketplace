package com.rest.marketplace.infrastructure.configuration.redis;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching  //activa el sistema de cache de Spring, sin esto las anotaciones @Cacheable no funcionan.
public class RedisConfig {

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

		// config por defecto para todos los caches (products, etc)
		RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofMinutes(10))  //define que el cache expira a los 10 minutos.
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair
								.fromSerializer(new StringRedisSerializer()) //para las keys significa que en Redis las keys van a ser strings legibles como products::1
				)
				.serializeValuesWith(
						RedisSerializationContext.SerializationPair
								.fromSerializer(new GenericJackson2JsonRedisSerializer())  //para los valores significa que los objetos se guardan como JSON en Redis, lo que te permite verlos en Redis y que sean legibles.
				)
				.disableCachingNullValues(); //evita que se cacheen resultados nulos, importante para no cachear un "producto no encontrado".

		// config específica para el tipo de cambio (1 hora)
		RedisCacheConfiguration exchangeRateConfig = defaultConfig.entryTtl(Duration.ofHours(1));

		return RedisCacheManager.builder(connectionFactory)
				.cacheDefaults(defaultConfig )
				.withCacheConfiguration("exchangeRate", exchangeRateConfig) // ← config particular
				.build();
	}
}
