package com.jdvergara.techtest.inventory_service.client;

import com.jdvergara.techtest.inventory_service.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class ProductClient {

    private final RestClient restClient;

    private final String productServiceBaseUrl;

    public ProductClient(
            @Value("${product-service.base-url}") String productServiceBaseUrl) {
        this.productServiceBaseUrl = productServiceBaseUrl;
        this.restClient = RestClient.builder()
                .baseUrl(productServiceBaseUrl)
                .build();
    }

    public ProductDto getProductById(Long productId) {
        try {
            return restClient.get()
                    .uri("/api/products/{id}", productId)
                    .retrieve()
                    .body(ProductDto.class);
        } catch (RestClientException ex) {
            throw new RuntimeException("Error calling product-service", ex);
        }
    }
}
