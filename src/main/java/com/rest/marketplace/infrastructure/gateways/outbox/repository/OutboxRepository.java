package com.rest.marketplace.infrastructure.gateways.outbox.repository;

import com.rest.marketplace.domain.enums.outbox.OutboxStatus;
import com.rest.marketplace.infrastructure.gateways.outbox.entity.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEntity, UUID> {

	List<OutboxEntity> findByStatus(OutboxStatus status);
}
