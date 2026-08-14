package com.rest.marketplace.application.usecases.auth;

import com.rest.marketplace.infrastructure.rest.auth.request.LoginRequest;
import com.rest.marketplace.infrastructure.rest.auth.response.AuthResponse;

public interface LoginUc {

	AuthResponse login(LoginRequest loginRequest);
}
