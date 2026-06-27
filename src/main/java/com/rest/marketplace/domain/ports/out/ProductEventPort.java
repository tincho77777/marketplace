package com.rest.marketplace.domain.ports.out;

import com.rest.marketplace.domain.models.events.ProductCreatedEvent;

public interface ProductEventPort {

	void publishProductCreated(ProductCreatedEvent event);
}
