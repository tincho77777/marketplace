package com.rest.marketplace.infrastructure.gateways.idempotency.mapper;

import com.rest.marketplace.domain.models.idempotency.Idempotency;
import com.rest.marketplace.infrastructure.gateways.idempotency.entity.IdempotencyEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IdempotencyMapper {

	public static Idempotency toDomain(IdempotencyEntity idempotencyEntity){
		return Idempotency.builder()
				.id(idempotencyEntity.getId())
				.idempotencyKey(idempotencyEntity.getIdempotencyKey())
				.response(idempotencyEntity.getResponse())
				.createdAt(idempotencyEntity.getCreatedAt())
				.build();
	}

	public static IdempotencyEntity toEntity(Idempotency idempotency) {
		return IdempotencyEntity.builder()
				.id(idempotency.getId())
				.idempotencyKey(idempotency.getIdempotencyKey())
				.response(idempotency.getResponse())
				.createdAt(idempotency.getCreatedAt())
				.build();
	}
}
