package com.rest.marketplace.domain.ports.outbox;

import com.rest.marketplace.domain.models.events.ProductCreatedEvent;
import com.rest.marketplace.domain.models.outbox.OutboxEvent;

import java.util.List;

public interface OutboxPort {

	void save(OutboxEvent outboxEvent);

	List<OutboxEvent> findPending();

	void markAsProcessed(OutboxEvent outboxEvent);

	void markAsFailed(OutboxEvent outboxEvent);

	void saveProductCreatedEvent(ProductCreatedEvent event);

}

