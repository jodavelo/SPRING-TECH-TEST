package com.jdvergara.techtest.inventory_service.config;

import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    private final ProductServiceClientProperties clientProps;
    private final ProductServiceApiKeyProperties apiKeyProps;

    @Bean
    public RestTemplate productRestTemplate() {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.of(clientProps.getConnectTimeoutMs(), TimeUnit.MILLISECONDS))
                .setResponseTimeout(Timeout.of(clientProps.getReadTimeoutMs(), TimeUnit.MILLISECONDS))
                .build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(20);
        cm.setDefaultMaxPerRoute(10);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        RestTemplate restTemplate = new RestTemplate(factory);

        // Interceptor que mete el API key en cada request
        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().add(apiKeyProps.getHeader(), apiKeyProps.getValue());
            return execution.execute(request, body);
        }));

        return restTemplate;
    }
}
