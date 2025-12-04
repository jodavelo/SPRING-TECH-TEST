package com.jdvergara.techtest.inventory_service.client;

import com.jdvergara.techtest.inventory_service.config.ProductServiceClientProperties;
import com.jdvergara.techtest.inventory_service.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductClient {

    private final RestTemplate productRestTemplate;
    private final ProductServiceClientProperties clientProps;

    @Value("${product-service.base-url}")
    private String baseUrl;

    public ProductDto getProductById(Long productId) {
        String url = String.format("%s/api/products/%d", baseUrl, productId);

        int attempt = 0;
        while (true) {
            try {
                attempt++;
                ResponseEntity<ProductDto> response =
                        productRestTemplate.getForEntity(url, ProductDto.class);
                return response.getBody();
            } catch (ResourceAccessException ex) {
                // Errores típicos de timeout/conexión
                log.warn("Error llamando a product-service (intento {}/{}): {}",
                        attempt, clientProps.getMaxRetries(), ex.getMessage());

                if (attempt >= clientProps.getMaxRetries()) {
                    log.error("Se alcanzó el número máximo de reintentos hacia product-service");
                    throw ex;
                }
            }
        }
    }
}
