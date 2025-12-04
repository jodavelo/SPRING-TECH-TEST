package com.jdvergara.techtest.inventory_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "product-service.api-key")
public class ProductServiceApiKeyProperties {

    private String header;
    private String value;
}
