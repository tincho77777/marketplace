package com.rest.marketplace.domain.ports.user;

import com.rest.marketplace.domain.models.user.RefreshToken;
import com.rest.marketplace.domain.models.user.User;

import java.util.Optional;

public interface UserPort {

	User save(User user);

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	RefreshToken saveRefreshToken(RefreshToken refreshToken);

	Optional<RefreshToken> findRefreshToken(String token);

	void revokeRefreshToken(String token);
}
