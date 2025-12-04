package com.jdvergara.techtest.inventory_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdvergara.techtest.inventory_service.dto.InventoryItemRequest;
import com.jdvergara.techtest.inventory_service.dto.InventoryItemResponse;
import com.jdvergara.techtest.inventory_service.dto.PurchaseRequest;
import com.jdvergara.techtest.inventory_service.dto.PurchaseResponse;
import com.jdvergara.techtest.inventory_service.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InventoryService inventoryService;

    @Test
    void fullPurchaseFlow_shouldReturnPurchaseAndUpdateStock() throws Exception {
        InventoryItemResponse stockResponse = InventoryItemResponse.builder()
                .productId(6L)
                .quantity(10)
                .build();

        Mockito.when(inventoryService.createOrUpdateStock(any(InventoryItemRequest.class)))
                .thenReturn(stockResponse);

        PurchaseResponse purchaseResponse = PurchaseResponse.builder()
                .id(1L)
                .productId(6L)
                .quantity(3)
                .unitPrice(new BigDecimal("100.00"))
                .totalPrice(new BigDecimal("300.00"))
                .build();

        Mockito.when(inventoryService.purchase(any(PurchaseRequest.class)))
                .thenReturn(purchaseResponse);

        // 1) set stock
        InventoryItemRequest stockRequest = InventoryItemRequest.builder()
                .productId(6L)
                .quantity(10)
                .build();

        mockMvc.perform(post("/api/inventory/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(6L))
                .andExpect(jsonPath("$.quantity").value(10));

        // 2) purchase
        PurchaseRequest purchaseRequest = PurchaseRequest.builder()
                .productId(6L)
                .quantity(3)
                .build();

        mockMvc.perform(post("/api/inventory/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(6L))
                .andExpect(jsonPath("$.quantity").value(3))
                .andExpect(jsonPath("$.totalPrice").value(300.00));
    }
}
