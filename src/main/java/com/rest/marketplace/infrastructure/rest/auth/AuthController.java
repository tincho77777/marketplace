package com.rest.marketplace.infrastructure.rest.auth;

import com.rest.marketplace.application.usecases.auth.LoginUc;
import com.rest.marketplace.application.usecases.auth.RefreshTokenUc;
import com.rest.marketplace.application.usecases.auth.RegisterUc;
import com.rest.marketplace.infrastructure.rest.auth.request.LoginRequest;
import com.rest.marketplace.infrastructure.rest.auth.request.RefreshTokenRequest;
import com.rest.marketplace.infrastructure.rest.auth.request.RegisterRequest;
import com.rest.marketplace.infrastructure.rest.auth.response.AuthResponse;
import com.rest.marketplace.infrastructure.rest.auth.swagger.SwaggerAuthController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/marketplace/v1/auth")
@RequiredArgsConstructor
public class AuthController implements SwaggerAuthController {

	private final LoginUc loginUc;
	private final RefreshTokenUc refreshTokenUc;
	private final RegisterUc registerUc;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public AuthResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
		return registerUc.register(registerRequest);
	}

	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
		return loginUc.login(loginRequest);
	}

	@PostMapping("/refresh")
	public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
		return refreshTokenUc.refresh(refreshTokenRequest);
	}

}
