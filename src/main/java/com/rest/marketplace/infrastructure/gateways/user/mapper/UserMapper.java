package com.rest.marketplace.infrastructure.gateways.user.mapper;

import com.rest.marketplace.domain.models.user.RefreshToken;
import com.rest.marketplace.domain.models.user.User;
import com.rest.marketplace.infrastructure.gateways.user.entity.RefreshTokenEntity;
import com.rest.marketplace.infrastructure.gateways.user.entity.UserEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

	public static User toDomain(UserEntity entity){
		return User.builder()
				.id(entity.getId())
				.email(entity.getEmail())
				.password(entity.getPassword())
				.role(entity.getRole())
				.enabled(entity.getEnabled())
				.createdAt(entity.getCreatedAt())
				.build();
	}

	public static UserEntity toEntity(User user){
		return UserEntity.builder()
				.id(user.getId())
				.email(user.getEmail())
				.password(user.getPassword())
				.role(user.getRole())
				.enabled(user.getEnabled())
				.createdAt(user.getCreatedAt())
				.build();
	}

	public static RefreshToken toDomain(RefreshTokenEntity entity) {
		return RefreshToken.builder()
				.id(entity.getId())
				.token(entity.getToken())
				.userId(entity.getUserId())
				.expired(entity.getExpired())
				.revoked(entity.getRevoked())
				.createdAt(entity.getCreatedAt())
				.expiresAt(entity.getExpiresAt())
				.build();
	}

	public static RefreshTokenEntity toEntity(RefreshToken domain) {
		return RefreshTokenEntity.builder()
				.id(domain.getId())
				.token(domain.getToken())
				.userId(domain.getUserId())
				.expired(domain.getExpired())
				.revoked(domain.getRevoked())
				.createdAt(domain.getCreatedAt())
				.expiresAt(domain.getExpiresAt())
				.build();
	}
}
