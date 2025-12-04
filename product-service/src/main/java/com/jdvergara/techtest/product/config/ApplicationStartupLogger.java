package com.jdvergara.techtest.product.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationStartupLogger implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${server.port}")
    private int serverPort;

    @Value("${spring.application.name:product-service}")
    private String applicationName;

    @Value("${info.app.version:1.0.0}")
    private String appVersion;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("╔════════════════════════════════════════════════════════════════╗");
        log.info("║                                                                ║");
        log.info("║           🚀 PRODUCT SERVICE INICIADO CORRECTAMENTE 🚀         ║");
        log.info("║                                                                ║");
        log.info("╚════════════════════════════════════════════════════════════════╝");
        log.info("");
        log.info("📋 Información del Servicio:");
        log.info("   └─ Nombre: {}", applicationName);
        log.info("   └─ Versión: {}", appVersion);
        log.info("   └─ Puerto: {}", serverPort);
        log.info("");
        log.info("🌐 Endpoints disponibles:");
        log.info("   └─ Aplicación: http://localhost:{}", serverPort);
        log.info("   └─ Health Check: http://localhost:{}/actuator/health", serverPort);
        log.info("   └─ Info: http://localhost:{}/actuator/info", serverPort);
        log.info("   └─ Metrics: http://localhost:{}/actuator/metrics", serverPort);
        log.info("");
        log.info("✅ Sistema listo para recibir peticiones");
        log.info("════════════════════════════════════════════════════════════════");
    }
}
