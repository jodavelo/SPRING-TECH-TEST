package com.jdvergara.techtest.product.service;

import com.jdvergara.techtest.product.domain.Product;
import com.jdvergara.techtest.product.dto.ProductRequest;
import com.jdvergara.techtest.product.dto.ProductResponse;
import com.jdvergara.techtest.product.exception.DuplicateProductException;
import com.jdvergara.techtest.product.exception.ProductNotFoundException;
import com.jdvergara.techtest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse create(ProductRequest request) {
        // Verificar si ya existe un producto con el mismo SKU
        if (request.getSku() != null && productRepository.existsBySku(request.getSku())) {
            log.error("Intento de crear producto con SKU duplicado: {}", request.getSku());
            throw new DuplicateProductException(
                "Ya existe un producto con el SKU: " + request.getSku()
            );
        }

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .sku(request.getSku())
                .price(request.getPrice())
                .currency(request.getCurrency())
                .build();

        Product saved = productRepository.save(product);
        log.info("Producto creado exitosamente con ID: {} y SKU: {}", saved.getId(), saved.getSku());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product " + id + " not found"));
        return mapToResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .sku(product.getSku())
                .price(product.getPrice())
                .currency(product.getCurrency())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
