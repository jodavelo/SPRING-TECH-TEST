package com.jdvergara.techtest.inventory_service.repository;

import com.jdvergara.techtest.inventory_service.domain.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    Optional<InventoryItem> findByProductId(Long productId);
}
