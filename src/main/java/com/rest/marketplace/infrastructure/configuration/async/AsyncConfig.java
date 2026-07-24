package com.rest.marketplace.infrastructure.configuration.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync  // @EnableAsync activa el soporte de @Async en Spring. Sin esto la anotación no funciona
public class AsyncConfig {

	@Bean(name = "taskExecutor")
	public Executor taskExecutor(){
		var executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);   // hilos siempre activos
		executor.setMaxPoolSize(10);   // máximo de hilos en picos
		executor.setQueueCapacity(25);  // tareas en cola si todos los hilos están ocupados
		executor.setThreadNamePrefix("marketplace-async-");  // nombre visible en logs y Zipkin
		executor.setWaitForTasksToCompleteOnShutdown(true);  // espera que terminen al cerrar la app
		executor.setAwaitTerminationSeconds(30);
		executor.initialize();
		return executor;
	}
}
