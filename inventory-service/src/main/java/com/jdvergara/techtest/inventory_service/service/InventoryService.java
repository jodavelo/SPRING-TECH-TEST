package com.jdvergara.techtest.inventory_service.service;

import com.jdvergara.techtest.inventory_service.client.ProductClient;
import com.jdvergara.techtest.inventory_service.domain.InventoryItem;
import com.jdvergara.techtest.inventory_service.domain.Purchase;
import com.jdvergara.techtest.inventory_service.dto.*;
import com.jdvergara.techtest.inventory_service.exception.InsufficientStockException;
import com.jdvergara.techtest.inventory_service.repository.InventoryItemRepository;
import com.jdvergara.techtest.inventory_service.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryItemRepository inventoryItemRepository;
    private final PurchaseRepository purchaseRepository;
    private final ProductClient productClient;

    @Transactional
    public InventoryItemResponse createOrUpdateStock(InventoryItemRequest request) {
        InventoryItem item = inventoryItemRepository
                .findByProductId(request.getProductId())
                .orElseGet(() -> InventoryItem.builder()
                        .productId(request.getProductId())
                        .quantity(0)
                        .build());

        item.setQuantity(request.getQuantity());
        InventoryItem saved = inventoryItemRepository.save(item);

        return InventoryItemResponse.builder()
                .productId(saved.getProductId())
                .quantity(saved.getQuantity())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public InventoryItemResponse getStock(Long productId) {
        InventoryItem item = inventoryItemRepository
                .findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for product " + productId));

        return InventoryItemResponse.builder()
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    @Transactional
    public PurchaseResponse purchase(PurchaseRequest request) {
        // 1. Obtener producto (precio, etc.)
        ProductDto product = productClient.getProductById(request.getProductId());

        // 2. Verificar stock
        InventoryItem item = inventoryItemRepository
                .findByProductId(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for product " + request.getProductId()));

        if (item.getQuantity() < request.getQuantity()) {
            throw new InsufficientStockException("Not enough stock for product " + request.getProductId());
        }

        // 3. Descontar inventario
        item.setQuantity(item.getQuantity() - request.getQuantity());
        inventoryItemRepository.save(item);

        // 4. Registrar compra
        BigDecimal unitPrice = product.getPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));

        Purchase purchase = Purchase.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .unitPrice(unitPrice)
                .totalPrice(totalPrice)
                .build();

        Purchase saved = purchaseRepository.save(purchase);

        return PurchaseResponse.builder()
                .id(saved.getId())
                .productId(saved.getProductId())
                .quantity(saved.getQuantity())
                .unitPrice(saved.getUnitPrice())
                .totalPrice(saved.getTotalPrice())
                .purchasedAt(saved.getPurchasedAt())
                .build();
    }
}
