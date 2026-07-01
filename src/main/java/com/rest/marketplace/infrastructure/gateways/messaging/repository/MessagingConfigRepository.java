package com.rest.marketplace.infrastructure.gateways.messaging.repository;

import com.rest.marketplace.infrastructure.gateways.messaging.entity.MessagingConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MessagingConfigRepository extends JpaRepository<MessagingConfigEntity, Long> {

	@Query("SELECT m FROM MessagingConfigEntity m WHERE m.active = true ORDER BY m.id DESC LIMIT 1")
	Optional<MessagingConfigEntity> findActiveConfig();
}
