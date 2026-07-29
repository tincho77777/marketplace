package com.rest.marketplace.domain.ports.idempotency;

import com.rest.marketplace.domain.models.idempotency.Idempotency;

import java.util.Optional;

public interface IdempotencyPort {

	Optional<Idempotency> findByKey(String key);

	void save(Idempotency idempotency);
}
