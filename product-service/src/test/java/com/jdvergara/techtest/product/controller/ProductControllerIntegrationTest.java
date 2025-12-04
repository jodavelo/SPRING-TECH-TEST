package com.jdvergara.techtest.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdvergara.techtest.product.dto.ProductRequest;
import com.jdvergara.techtest.product.dto.ProductResponse;
import com.jdvergara.techtest.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // ignoramos el filtro de API key en este test
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void createProduct_andListProducts_shouldWork() throws Exception {
        ProductResponse created = ProductResponse.builder()
                .id(1L)
                .name("Laptop Gamer")
                .description("Laptop muy poderosa")
                .sku("LAP-001")
                .price(new BigDecimal("2500.00"))
                .currency("USD")
                .build();

        Mockito.when(productService.create(any(ProductRequest.class))).thenReturn(created);
        Mockito.when(productService.getAll()).thenReturn(List.of(created));

        ProductRequest request = ProductRequest.builder()
                .name("Laptop Gamer")
                .description("Laptop muy poderosa")
                .sku("LAP-001")
                .price(new BigDecimal("2500.00"))
                .currency("USD")
                .build();

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop Gamer"));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}
