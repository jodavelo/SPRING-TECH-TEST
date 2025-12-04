package com.jdvergara.techtest.product.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String HEADER_NAME = "X-API-KEY";

    private final ApiKeyProperties apiKeyProperties;

    /**
     * Solo se filtra las rutas que empiezan por /api/.
     * Swagger (/swagger-ui/**), OpenAPI (/v3/api-docs/**),
     * actuator (/actuator/**), etc., quedan libres.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !path.startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String apiKey = request.getHeader(HEADER_NAME);

        if (!StringUtils.hasText(apiKey)
                || !apiKeyProperties.getValue().equals(apiKey)) {

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("""
                {
                  "status": 401,
                  "message": "Invalid or missing API key"
                }
                """);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
