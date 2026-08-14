package com.rest.marketplace.infrastructure.configuration.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain) throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");

		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		final String jwt = authHeader.substring(7);

		try {
			final String userEmail = jwtService.extractUsername(jwt);

			if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				var userDetails = userDetailsService.loadUserByUsername(userEmail);

				if(jwtService.isTokenValid(jwt, userDetails)) {
					var authToken = new UsernamePasswordAuthenticationToken(
							userDetails, // el usuario autenticado
							null, // credentials (null porque ya validamos el token)
							userDetails.getAuthorities() // los roles
					);
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
					log.debug("✅ Usuario autenticado: {}", userEmail);
				}
			}
		} catch (Exception e) {
			log.error("❌ Error validando JWT: {}", e.getMessage());
		}
		filterChain.doFilter(request, response);
	}
}
