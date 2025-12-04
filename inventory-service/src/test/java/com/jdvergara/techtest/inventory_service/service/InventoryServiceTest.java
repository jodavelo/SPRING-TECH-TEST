package com.jdvergara.techtest.inventory_service.service;

import com.jdvergara.techtest.inventory_service.client.ProductClient;
import com.jdvergara.techtest.inventory_service.domain.InventoryItem;
import com.jdvergara.techtest.inventory_service.domain.Purchase;
import com.jdvergara.techtest.inventory_service.dto.*;
import com.jdvergara.techtest.inventory_service.exception.InsufficientStockException;
import com.jdvergara.techtest.inventory_service.repository.InventoryItemRepository;
import com.jdvergara.techtest.inventory_service.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    private InventoryItemRepository inventoryItemRepository;
    private PurchaseRepository purchaseRepository;
    private ProductClient productClient;
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        inventoryItemRepository = mock(InventoryItemRepository.class);
        purchaseRepository = mock(PurchaseRepository.class);
        productClient = mock(ProductClient.class);
        inventoryService = new InventoryService(inventoryItemRepository, purchaseRepository, productClient);
    }

    @Test
    void createOrUpdateStock_shouldCreateNewItemWhenNotExists() {
        InventoryItemRequest request = InventoryItemRequest.builder()
                .productId(1L)
                .quantity(10)
                .build();

        when(inventoryItemRepository.findByProductId(1L)).thenReturn(Optional.empty());

        InventoryItem saved = InventoryItem.builder()
                .id(1L)
                .productId(1L)
                .quantity(10)
                .build();

        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(saved);

        InventoryItemResponse response = inventoryService.createOrUpdateStock(request);

        assertThat(response.getProductId()).isEqualTo(1L);
        assertThat(response.getQuantity()).isEqualTo(10);
    }

    @Test
    void getStock_shouldReturnInventory_whenExists() {
        InventoryItem item = InventoryItem.builder()
                .id(1L)
                .productId(5L)
                .quantity(7)
                .build();

        when(inventoryItemRepository.findByProductId(5L)).thenReturn(Optional.of(item));

        InventoryItemResponse response = inventoryService.getStock(5L);

        assertThat(response.getProductId()).isEqualTo(5L);
        assertThat(response.getQuantity()).isEqualTo(7);
    }

    @Test
    void purchase_shouldSucceed_whenStockIsEnough() {
        PurchaseRequest request = PurchaseRequest.builder()
                .productId(6L)
                .quantity(3)
                .build();

        ProductDto product = ProductDto.builder()
                .id(6L)
                .name("Mouse Gamer")
                .price(new BigDecimal("100.00"))
                .currency("USD")
                .build();

        InventoryItem item = InventoryItem.builder()
                .id(1L)
                .productId(6L)
                .quantity(10)
                .build();

        when(productClient.getProductById(6L)).thenReturn(product);
        when(inventoryItemRepository.findByProductId(6L)).thenReturn(Optional.of(item));

        Purchase saved = Purchase.builder()
                .id(1L)
                .productId(6L)
                .quantity(3)
                .unitPrice(new BigDecimal("100.00"))
                .totalPrice(new BigDecimal("300.00"))
                .build();

        when(purchaseRepository.save(any(Purchase.class))).thenReturn(saved);

        PurchaseResponse response = inventoryService.purchase(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTotalPrice()).isEqualTo(new BigDecimal("300.00"));

        // se descuenta el stock
        assertThat(item.getQuantity()).isEqualTo(7);
        verify(inventoryItemRepository).save(item);
    }

    @Test
    void purchase_shouldFail_whenStockIsInsufficient() {
        PurchaseRequest request = PurchaseRequest.builder()
                .productId(6L)
                .quantity(100)
                .build();

        ProductDto product = ProductDto.builder()
                .id(6L)
                .name("Mouse Gamer")
                .price(new BigDecimal("100.00"))
                .currency("USD")
                .build();

        InventoryItem item = InventoryItem.builder()
                .id(1L)
                .productId(6L)
                .quantity(5)
                .build();

        when(productClient.getProductById(6L)).thenReturn(product);
        when(inventoryItemRepository.findByProductId(6L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> inventoryService.purchase(request))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Not enough stock");
    }

    @Test
    void purchase_shouldFail_whenInventoryNotExists() {
        PurchaseRequest request = PurchaseRequest.builder()
                .productId(999L)
                .quantity(1)
                .build();

        ProductDto product = ProductDto.builder()
                .id(999L)
                .name("Fake product")
                .price(new BigDecimal("1.00"))
                .currency("USD")
                .build();

        when(productClient.getProductById(999L)).thenReturn(product);
        when(inventoryItemRepository.findByProductId(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inventoryService.purchase(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Inventory not found");
    }
}
