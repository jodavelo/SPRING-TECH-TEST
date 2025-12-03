package com.jdvergara.techtest.inventory_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryItemRequest {

    @NotNull
    private Long productId;

    @NotNull
    @Min(0)
    private Integer quantity;
}
