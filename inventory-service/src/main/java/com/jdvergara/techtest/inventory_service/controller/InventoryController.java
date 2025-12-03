package com.jdvergara.techtest.inventory_service.controller;

import com.jdvergara.techtest.inventory_service.dto.*;
import com.jdvergara.techtest.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/stock")
    public ResponseEntity<InventoryItemResponse> createOrUpdateStock(
            @Valid @RequestBody InventoryItemRequest request) {
        return ResponseEntity.ok(inventoryService.createOrUpdateStock(request));
    }

    @GetMapping("/stock/{productId}")
    public ResponseEntity<InventoryItemResponse> getStock(
            @PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getStock(productId));
    }

    @PostMapping("/purchases")
    public ResponseEntity<PurchaseResponse> createPurchase(
            @Valid @RequestBody PurchaseRequest request) {
        return ResponseEntity.ok(inventoryService.purchase(request));
    }
}
