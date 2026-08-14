package com.rest.marketplace.infrastructure.rest.auth.request;

import com.rest.marketplace.domain.enums.user.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
		@NotBlank String email,
		@NotBlank String password,
		@NotNull Role role) {
}
