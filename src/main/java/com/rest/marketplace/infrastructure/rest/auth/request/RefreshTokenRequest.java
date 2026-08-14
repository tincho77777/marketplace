package com.rest.marketplace.infrastructure.rest.auth.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
		@NotBlank String refreshToken) {
}
