package com.rest.marketplace.domain.models.user;

import com.rest.marketplace.domain.enums.user.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private UUID id;
	private String email;
	private String password;
	private Role role;
	private Boolean enabled;
	private LocalDateTime createdAt;
}
