package com.rest.marketplace.domain.models.outbox;

import com.rest.marketplace.domain.enums.outbox.OutboxStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {

	private UUID id;
	private String eventType;
	private String payload;
	private OutboxStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime processedAt;
}
