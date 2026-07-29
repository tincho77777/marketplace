package com.rest.marketplace.infrastructure.gateways.idempotency.repository;

import com.rest.marketplace.infrastructure.gateways.idempotency.entity.IdempotencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IdempotencyRepository extends JpaRepository<IdempotencyEntity, UUID> {

	Optional<IdempotencyEntity> findByIdempotencyKey(String idempotencyKey);
}
