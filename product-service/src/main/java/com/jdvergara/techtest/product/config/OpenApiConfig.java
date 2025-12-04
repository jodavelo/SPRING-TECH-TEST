package com.jdvergara.techtest.product.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Service API")
                        .description("API del microservicio de productos para la prueba técnica")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("José Daniel Vergara Lozano")
                                .email("jodavelo30@gmail.com")));
    }
}
