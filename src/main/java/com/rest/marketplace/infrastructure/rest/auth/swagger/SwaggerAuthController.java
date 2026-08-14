package com.rest.marketplace.infrastructure.rest.auth.swagger;

import com.rest.marketplace.infrastructure.rest.auth.request.LoginRequest;
import com.rest.marketplace.infrastructure.rest.auth.request.RefreshTokenRequest;
import com.rest.marketplace.infrastructure.rest.auth.request.RegisterRequest;
import com.rest.marketplace.infrastructure.rest.auth.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Auth Resource", description = "Operaciones relacionadas autenticacion")
public interface SwaggerAuthController {

	@Operation(summary = "Registro de usuario", description = "Registra un nuevo usuario en el sistema y devuelve los tokens de acceso")
	@ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente.")
	@ApiResponse(responseCode = "400", description = "El request enviado es inválido o el email ya existe.")
	@ApiResponse(responseCode = "500", description = "Error interno del servidor.")
	AuthResponse register(
			@Valid
			@RequestBody(description = "Datos necesarios para registrar un usuario", required = true)
			RegisterRequest request
	);

	@Operation(summary = "Login de usuario", description = "Autentica un usuario y devuelve los tokens de acceso")
	@ApiResponse(responseCode = "200", description = "Operación ejecutada exitosamente.")
	@ApiResponse(responseCode = "400", description = "Credenciales inválidas.")
	@ApiResponse(responseCode = "500", description = "Error interno del servidor.")
	AuthResponse login(
			@Valid
			@RequestBody(description = "Credenciales del usuario", required = true)
			LoginRequest request
	);

	@Operation(summary = "Renovar token", description = "Genera un nuevo access token a partir de un refresh token válido")
	@ApiResponse(responseCode = "200", description = "Token renovado exitosamente.")
	@ApiResponse(responseCode = "400", description = "Refresh token inválido o expirado.")
	@ApiResponse(responseCode = "500", description = "Error interno del servidor.")
	AuthResponse refresh(
			@Valid
			@RequestBody(description = "Refresh token para renovar el access token", required = true)
			RefreshTokenRequest request
	);
}
