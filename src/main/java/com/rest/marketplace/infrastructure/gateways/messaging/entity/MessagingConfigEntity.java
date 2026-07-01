package com.rest.marketplace.infrastructure.gateways.messaging.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messaging_config")
public class MessagingConfigEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String provider;

	@Column(nullable = false)
	private Boolean active;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
}
