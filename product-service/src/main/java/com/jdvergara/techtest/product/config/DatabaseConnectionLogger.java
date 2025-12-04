package com.jdvergara.techtest.product.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@Slf4j
@Component
public class DatabaseConnectionLogger implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private DataSource dataSource;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            log.info("════════════════════════════════════════════════════════════════");
            log.info("✅ CONEXIÓN A BASE DE DATOS EXITOSA");
            log.info("════════════════════════════════════════════════════════════════");
            log.info("📊 Información de la Base de Datos:");
            log.info("   └─ Tipo: {}", metaData.getDatabaseProductName());
            log.info("   └─ Versión: {}", metaData.getDatabaseProductVersion());
            log.info("   └─ URL: {}", metaData.getURL());
            log.info("   └─ Usuario: {}", metaData.getUserName());
            log.info("   └─ Driver: {} v{}", metaData.getDriverName(), metaData.getDriverVersion());
            log.info("   └─ Catálogo actual: {}", connection.getCatalog());
            log.info("════════════════════════════════════════════════════════════════");
            
        } catch (Exception e) {
            log.error("════════════════════════════════════════════════════════════════");
            log.error("❌ ERROR AL CONECTAR CON LA BASE DE DATOS");
            log.error("════════════════════════════════════════════════════════════════");
            log.error("Error: {}", e.getMessage());
            log.error("════════════════════════════════════════════════════════════════");
        }
    }
}
