package com.rest.marketplace.infrastructure.gateways.idempotency.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "idempotency_keys")
public class IdempotencyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "idempotency_key", nullable = false, unique = true)  // unique = true para que la DB garantice unicidad además del índice
	private String idempotencyKey;

	@Column(nullable = false, columnDefinition = "TEXT")  // columnDefinition = "TEXT" porque las respuestas pueden ser largas
	private String response;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

}
