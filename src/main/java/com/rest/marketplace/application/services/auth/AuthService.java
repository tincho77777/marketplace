package com.rest.marketplace.application.services.auth;

import com.rest.marketplace.application.usecases.auth.LoginUc;
import com.rest.marketplace.application.usecases.auth.RefreshTokenUc;
import com.rest.marketplace.application.usecases.auth.RegisterUc;
import com.rest.marketplace.domain.enums.user.Role;
import com.rest.marketplace.domain.exceptions.BadRequestException;
import com.rest.marketplace.domain.models.user.RefreshToken;
import com.rest.marketplace.domain.models.user.User;
import com.rest.marketplace.domain.ports.user.UserPort;
import com.rest.marketplace.infrastructure.configuration.security.JwtService;
import com.rest.marketplace.infrastructure.rest.auth.request.LoginRequest;
import com.rest.marketplace.infrastructure.rest.auth.request.RefreshTokenRequest;
import com.rest.marketplace.infrastructure.rest.auth.request.RegisterRequest;
import com.rest.marketplace.infrastructure.rest.auth.response.AuthResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService implements RegisterUc, LoginUc, RefreshTokenUc {

	private final UserPort userPort;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;

	@Transactional
	@Override
	public AuthResponse register(RegisterRequest registerRequest) {
		if(userPort.existsByEmail(registerRequest.email())) {
			throw new BadRequestException("El email ya está registrado: " + registerRequest.email());
		}

		var user = User.builder()
				.email(registerRequest.email())
				.password(passwordEncoder.encode(registerRequest.password()))
				.role(registerRequest.role() != null ? registerRequest.role() : Role.USER)
				.enabled(true)
				.createdAt(LocalDateTime.now())
				.build();

		var savedUser = userPort.save(user);
		log.info("✅ Usuario registrado: {}", savedUser.getEmail());

		var userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
		var accessToken = jwtService.generateAccessToken(userDetails);
		var refreshToken = jwtService.generateRefreshToken(userDetails);

		saveRefreshToken(savedUser, refreshToken);

		return AuthResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.email(user.getEmail())
				.role(user.getRole().name())
				.build();
	}

	@Override
	public AuthResponse login(LoginRequest loginRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				loginRequest.email(), loginRequest.password()));

		var userDetails = userDetailsService.loadUserByUsername(loginRequest.email());
		var accessToken = jwtService.generateAccessToken(userDetails);
		var refreshToken = jwtService.generateRefreshToken(userDetails);

		var user = userPort.findByEmail(loginRequest.email()).orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

		saveRefreshToken(user, refreshToken);

		log.info("✅ Login exitoso: {}", loginRequest.email());

		return AuthResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.email(user.getEmail())
				.role(user.getRole().name())
				.build();
	}

	@Override
	public AuthResponse refresh(RefreshTokenRequest refreshTokenRequest) {
		var storedToken = userPort.findRefreshToken(refreshTokenRequest.refreshToken())
				.orElseThrow(() -> new BadRequestException("Refresh token inválido"));

		if(storedToken.getExpired() || storedToken.getRevoked()) {
			throw new BadRequestException("Refresh token expirado o revocado");
		}

		var user = userPort.findByEmail(jwtService.extractUsername(refreshTokenRequest.refreshToken()))
				.orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

		userPort.revokeRefreshToken(refreshTokenRequest.refreshToken());

		var userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		var newAccessToken = jwtService.generateAccessToken(userDetails);
		var newRefreshToken = jwtService.generateRefreshToken(userDetails);

		saveRefreshToken(user, newRefreshToken);

		log.info("✅ Token renovado para: {}", user.getEmail());

		return AuthResponse.builder()
				.accessToken(newAccessToken)
				.refreshToken(newRefreshToken)
				.email(user.getEmail())
				.role(user.getRole().name())
				.build();
	}

	private void saveRefreshToken(User user, String refreshToken) {
		userPort.saveRefreshToken(RefreshToken.builder()
				.token(refreshToken)
				.userId(user.getId())
				.expired(false)
				.revoked(false)
				.createdAt(LocalDateTime.now())
				.expiresAt(LocalDateTime.now())
				.build());
	}

}
