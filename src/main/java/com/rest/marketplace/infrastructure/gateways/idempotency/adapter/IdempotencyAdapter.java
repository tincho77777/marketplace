package com.rest.marketplace.infrastructure.gateways.idempotency.adapter;

import com.rest.marketplace.domain.models.idempotency.Idempotency;
import com.rest.marketplace.domain.ports.idempotency.IdempotencyPort;
import com.rest.marketplace.infrastructure.gateways.idempotency.mapper.IdempotencyMapper;
import com.rest.marketplace.infrastructure.gateways.idempotency.repository.IdempotencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IdempotencyAdapter implements IdempotencyPort {

	private final IdempotencyRepository idempotencyRepository;

	@Override
	public Optional<Idempotency> findByKey(String key) {
		return idempotencyRepository.findByIdempotencyKey(key).map(IdempotencyMapper::toDomain);
	}

	@Override
	public void save(Idempotency idempotency) {
		idempotencyRepository.save(IdempotencyMapper.toEntity(idempotency));
	}
}
