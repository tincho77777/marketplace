package com.rest.marketplace.infrastructure.rest.auth.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
		@NotBlank String email,
		@NotBlank String password) {
}
