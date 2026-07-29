package com.rest.marketplace.domain.models.idempotency;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Idempotency {

	private UUID id;
	private String idempotencyKey;
	private String response;
	private LocalDateTime createdAt;
}
