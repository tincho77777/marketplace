package com.rest.marketplace.application.usecases.auth;

import com.rest.marketplace.infrastructure.rest.auth.request.RefreshTokenRequest;
import com.rest.marketplace.infrastructure.rest.auth.response.AuthResponse;

public interface RefreshTokenUc {

	AuthResponse refresh(RefreshTokenRequest refreshTokenRequest);
}
