package com.rest.marketplace.domain.models.user;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

	private UUID id;
	private String token;
	private UUID userId;
	private Boolean expired;
	private Boolean revoked;
	private LocalDateTime createdAt;
	private LocalDateTime expiresAt;
}
