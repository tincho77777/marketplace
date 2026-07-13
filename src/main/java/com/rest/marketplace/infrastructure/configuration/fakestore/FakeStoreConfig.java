package com.rest.marketplace.infrastructure.configuration.fakestore;

import com.rest.marketplace.infrastructure.gateways.clients.fakestore.FakeStoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class FakeStoreConfig {

	@Value("${fake-store.url}")
	private String fakeStoreUrl;

	//Acá está la diferencia clave con Feign: con HttpInterface registrás el cliente manualmente usando HttpServiceProxyFactory. Con Feign Spring lo hacía automáticamente con @EnableFeignClients. Más código de setup pero sin dependencia externa.
	@Bean
	public FakeStoreClient fakeStoreClient() {

		WebClient webClient = WebClient.builder()
				.baseUrl(fakeStoreUrl)
				.build();

		HttpServiceProxyFactory factory = HttpServiceProxyFactory
				.builderFor(WebClientAdapter.create(webClient))
				.build();

		return factory.createClient(FakeStoreClient.class);
	}
}
