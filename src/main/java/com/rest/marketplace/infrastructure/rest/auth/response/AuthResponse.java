package com.rest.marketplace.infrastructure.rest.auth.response;

import lombok.Builder;

@Builder
public record AuthResponse(
		String accessToken,
		String refreshToken,
		String email,
		String role) {
}
