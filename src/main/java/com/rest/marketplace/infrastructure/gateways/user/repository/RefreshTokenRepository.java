package com.rest.marketplace.infrastructure.gateways.user.repository;

import com.rest.marketplace.infrastructure.gateways.user.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

	Optional<RefreshTokenEntity> findByToken(String token);

	@Modifying
	@Query("UPDATE RefreshTokenEntity r SET r.revoked = true, r.expired = true WHERE r.token = :token")
	void revokeToken(String token);
}
