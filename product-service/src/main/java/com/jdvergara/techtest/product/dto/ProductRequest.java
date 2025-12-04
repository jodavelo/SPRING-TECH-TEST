package com.jdvergara.techtest.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank
    private String name;

    private String description;

    @Size(max = 100)
    private String sku;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @NotBlank
    @Size(min = 3, max = 3)
    private String currency;
}
