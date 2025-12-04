package com.jdvergara.techtest.inventory_service.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventoryServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Service API")
                        .description("API del microservicio de inventario y compras para la prueba técnica")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("José Daniel Vergara Lozano")
                                .email("jodavelo30@gmail.com")));
    }
}
