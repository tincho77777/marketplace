package com.rest.marketplace.infrastructure.gateways.product.repository;

import com.rest.marketplace.infrastructure.gateways.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
