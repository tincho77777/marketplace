package com.rest.marketplace.infrastructure.rest.filter;

import com.rest.marketplace.domain.models.idempotency.Idempotency;
import com.rest.marketplace.domain.ports.idempotency.IdempotencyPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class IdempotencyFilter extends OncePerRequestFilter {

	private final IdempotencyPort idempotencyPort;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                @NonNull HttpServletResponse response,
	                                @NonNull FilterChain filterChain) throws ServletException, IOException {

		var idempotencyKey = request.getHeader("Idempotency-Key");

		// solo aplica a POST requests con el header
		if (idempotencyKey == null || !request.getMethod().equals("POST")) {
			filterChain.doFilter(request, response);
			return; // importante: detener ejecución para no intentar guardar una key nula
		}

		// busca si ya existe una respuesta para esta key
		var existing = idempotencyPort.findByKey(idempotencyKey);
		if (existing.isPresent()) {
			log.info("♻️ Request idempotente detectada. Key: {}", idempotencyKey);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.getWriter().write(existing.get().getResponse());
			return; // ya devolvimos la respuesta almacenada, terminamos el filter
		}

		// captura la respuesta para guardarla
		var cachedResponse = new CachedBodyHttpServletResponse(response);
		filterChain.doFilter(request, cachedResponse);

		// guarda la respuesta si fue exitosa
		if (cachedResponse.getStatus() < 400) {
			idempotencyPort.save(Idempotency.builder()
					.idempotencyKey(idempotencyKey)
					.response(cachedResponse.getCachedBody())
					.createdAt(LocalDateTime.now())
					.build());

			log.info("💾 Respuesta guardada para key: {}", idempotencyKey);
		}

		// escribe la respuesta real
		response.getOutputStream().write(cachedResponse.getCachedBody().getBytes());
	}
}
