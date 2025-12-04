package com.jdvergara.techtest.product.service;

import com.jdvergara.techtest.product.domain.Product;
import com.jdvergara.techtest.product.dto.ProductRequest;
import com.jdvergara.techtest.product.dto.ProductResponse;
import com.jdvergara.techtest.product.exception.ProductNotFoundException;
import com.jdvergara.techtest.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void create_shouldPersistProductAndReturnResponse() {
        // arrange
        ProductRequest request = ProductRequest.builder()
                .name("Laptop Gamer")
                .description("Laptop muy poderosa")
                .sku("LAP-001")
                .price(new BigDecimal("2500.00"))
                .currency("USD")
                .build();

        Product saved = Product.builder()
                .id(1L)
                .name(request.getName())
                .description(request.getDescription())
                .sku(request.getSku())
                .price(request.getPrice())
                .currency(request.getCurrency())
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(saved);

        // act
        ProductResponse response = productService.create(request);

        // assert
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());

        Product toSave = captor.getValue();
        assertThat(toSave.getId()).isNull(); // id lo asigna la BD
        assertThat(toSave.getName()).isEqualTo("Laptop Gamer");

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Laptop Gamer");
    }

    @Test
    void getById_shouldReturnProduct_whenExists() {
        Product product = Product.builder()
                .id(10L)
                .name("Mouse Gamer")
                .price(new BigDecimal("100.00"))
                .currency("USD")
                .build();

        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getById(10L);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("Mouse Gamer");
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getById(99L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product 99 not found");
    }

    @Test
    void getAll_shouldReturnListOfProducts() {
        Product p1 = Product.builder().id(1L).name("P1").build();
        Product p2 = Product.builder().id(2L).name("P2").build();

        when(productRepository.findAll()).thenReturn(List.of(p1, p2));

        List<ProductResponse> responses = productService.getAll();

        assertThat(responses).hasSize(2);
        assertThat(responses).extracting("id").containsExactly(1L, 2L);
    }
}
