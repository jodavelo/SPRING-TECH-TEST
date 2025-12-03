package com.jdvergara.techtest.inventory_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private String sku;
    private BigDecimal price;
    private String currency;
}