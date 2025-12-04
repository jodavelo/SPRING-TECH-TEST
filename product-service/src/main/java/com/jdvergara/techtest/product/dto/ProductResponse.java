package com.jdvergara.techtest.product.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private String sku;
    private BigDecimal price;
    private String currency;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
