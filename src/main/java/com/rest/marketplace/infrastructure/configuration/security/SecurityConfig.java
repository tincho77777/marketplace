package com.rest.marketplace.infrastructure.configuration.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final UserDetailsServiceImpl userDetailsService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		httpSecurity
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)  // ← esto deshabilita el /login de Spring
				.httpBasic(AbstractHttpConfigurer::disable)  // ← esto deshabilita el basic auth
				.exceptionHandling(ex -> ex
						.accessDeniedHandler(accessDeniedHandler())   // ← handler error 403 porque no llega a interceptarlo el handler comun
						.authenticationEntryPoint(authenticationEntryPoint()))  // ← handler error 401 porque no llega a interceptarlo el handler comun
				.authorizeHttpRequests(auth -> auth
						// endpoints públicos
						.requestMatchers("/marketplace/v1/auth/**").permitAll()
						.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/webjars/**", "/error").permitAll()
						.requestMatchers("/actuator/**").permitAll()

						// endpoints solo ADMIN
						.requestMatchers(HttpMethod.POST, "/marketplace/v1/products").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/marketplace/v1/products/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/marketplace/v1/products/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/marketplace/v1/products/import").hasRole("ADMIN")

						// endpoints ADMIN y USER
						.requestMatchers(HttpMethod.GET, "/marketplace/v1/products/**").hasAnyRole("ADMIN", "USER")

						// cualquier otra request requiere autenticación
						.anyRequest().authenticated()
				)
				// no usamos sesiones porque JWT es stateless
				.sessionManagement(session ->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.authenticationProvider(authenticationProvider())
				// nuestro filtro JWT corre antes del filtro de Spring
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return (request, response, ex) -> {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("application/json");
			response.getWriter().write("""
            {
                "code": 403,
                "detail": "ACCESS_DENIED",
                "message": "No tenés permisos para acceder a este recurso",
                "timestamp": "%s",
                "path": "%s"
            }
            """.formatted(LocalDateTime.now(), request.getRequestURI()));
		};
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, ex) -> {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.getWriter().write("""
            {
                "code": 401,
                "detail": "UNAUTHORIZED",
                "message": "Token inválido, expirado o no proporcionado",
                "timestamp": "%s",
                "path": "%s"
            }
            """.formatted(LocalDateTime.now(), request.getRequestURI()));
		};
	}
}
