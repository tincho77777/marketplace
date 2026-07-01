package com.rest.marketplace.domain.models.messaging;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessagingConfig {

	private Long id;
	private String provider;
	private Boolean active;
	private LocalDateTime updatedAt;
}
