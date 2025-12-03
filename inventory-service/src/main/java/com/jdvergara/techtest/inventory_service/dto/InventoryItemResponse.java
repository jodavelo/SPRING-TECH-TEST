package com.jdvergara.techtest.inventory_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class InventoryItemResponse {

    private Long productId;
    private Integer quantity;
    private OffsetDateTime updatedAt;
}
