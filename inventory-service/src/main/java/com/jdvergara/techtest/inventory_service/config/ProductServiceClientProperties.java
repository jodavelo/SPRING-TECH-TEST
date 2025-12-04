package com.jdvergara.techtest.inventory_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "product-service.client")
public class ProductServiceClientProperties {

    private int connectTimeoutMs;
    private int readTimeoutMs;
    private int maxRetries;
}
