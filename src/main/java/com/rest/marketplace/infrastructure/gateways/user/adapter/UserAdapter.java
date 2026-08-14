package com.rest.marketplace.infrastructure.gateways.user.adapter;

import com.rest.marketplace.domain.models.user.RefreshToken;
import com.rest.marketplace.domain.models.user.User;
import com.rest.marketplace.domain.ports.user.UserPort;
import com.rest.marketplace.infrastructure.gateways.user.mapper.UserMapper;
import com.rest.marketplace.infrastructure.gateways.user.repository.RefreshTokenRepository;
import com.rest.marketplace.infrastructure.gateways.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserPort {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public User save(User user) {
		return UserMapper.toDomain(userRepository.save(UserMapper.toEntity(user)));
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email).map(UserMapper::toDomain);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public RefreshToken saveRefreshToken(RefreshToken refreshToken) {
		return UserMapper.toDomain(refreshTokenRepository.save(UserMapper.toEntity(refreshToken)));
	}

	@Override
	public Optional<RefreshToken> findRefreshToken(String token) {
		return refreshTokenRepository.findByToken(token).map(UserMapper::toDomain);
	}

	@Override
	@Transactional
	public void revokeRefreshToken(String token) {
		refreshTokenRepository.revokeToken(token);
	}
}
