package com.jdvergara.techtest.product.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security.api-key")
public class ApiKeyProperties {

    /**
     * Nombre del header que debe venir en la petición, por ejemplo: X-API-KEY
     */
    private String header;

    /**
     * Valor esperado del API key (my-super-secret-key o el valor del env INTERNAL_API_KEY)
     */
    private String value;
}
