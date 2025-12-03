package com.jdvergara.techtest.inventory_service.repository;

import com.jdvergara.techtest.inventory_service.domain.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByProductId(Long productId);
}
