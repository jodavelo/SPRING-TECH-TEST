package com.jdvergara.techtest.inventory_service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component("customHealthCheck")
public class HealthCheckConfig implements HealthIndicator {

    private final LocalDateTime startTime = LocalDateTime.now();

    @Override
    public Health health() {
        try {
            // Aquí puedes agregar validaciones personalizadas
            long uptime = java.time.Duration.between(startTime, LocalDateTime.now()).getSeconds();
            
            return Health.up()
                    .withDetail("status", "Servicio operativo")
                    .withDetail("service", "inventory-service")
                    .withDetail("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .withDetail("uptime_seconds", uptime)
                    .withDetail("uptime_formatted", formatUptime(uptime))
                    .build();
        } catch (Exception e) {
            log.error("Error en health check: {}", e.getMessage());
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }

    private String formatUptime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%dh %dm %ds", hours, minutes, secs);
    }
}
