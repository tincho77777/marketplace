package com.rest.marketplace.application.usecases.auth;

import com.rest.marketplace.infrastructure.rest.auth.request.RegisterRequest;
import com.rest.marketplace.infrastructure.rest.auth.response.AuthResponse;

public interface RegisterUc {

	AuthResponse register(RegisterRequest registerRequest);
}
