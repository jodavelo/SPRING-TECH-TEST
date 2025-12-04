# Prueba técnica Backend – Microservicios con Spring Boot

Este repositorio contiene mi solución a la prueba técnica de backend, implementada con una arquitectura basada en **microservicios** usando **Spring Boot** y **MySQL**.

Los dos microservicios principales son:

- `product-service`: gestión del catálogo de productos.
- `inventory-service`: gestión del inventario y del flujo de compras (descuenta stock y registra compras).

Ambos servicios exponen APIs REST y se comunican entre sí vía HTTP usando JSON.  
El `inventory-service` consume el `product-service` utilizando **API key**, timeouts y reintentos básicos.

---

## Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3.4.x**
  - Spring Web
  - Spring Data JPA
  - Validation
  - Spring Boot Actuator
- **MySQL 8** (`product_db` e `inventory_db`)
- **Maven**
- **Postman** para pruebas manuales de la API
- **VS Code** como entorno de desarrollo (Windows 11)

Inicialmente empecé con PostgreSQL en Docker, pero para simplificar la prueba y aprovechar mi entorno actual terminé dejando la solución final con **MySQL**.

---

## Decisiones de arquitectura

### Microservicios y bases de datos

- Cada microservicio es una aplicación **Spring Boot** independiente.
- Cada servicio tiene su propia base de datos lógica:
  - `product-service` → BD `product_db`
  - `inventory-service` → BD `inventory_db`
- No se comparten tablas entre servicios; la integración se hace únicamente por **HTTP**.

### Comunicación entre microservicios

- El `inventory-service` necesita información del producto (precio, nombre, etc.) para poder registrar compras.
- La comunicación es **sincrónica** vía HTTP:
  - `inventory-service` llama a `product-service` a través de un cliente `RestTemplate` (`ProductClient`).
  - El base URL del `product-service` se configura en `application.yaml`:
    ```yaml
    product-service:
      base-url: http://localhost:8081
    ```
- El cuerpo y las respuestas se manejan en JSON.

### Seguridad: API key entre servicios

- Para la autenticación básica entre servicios definí una API key sencilla:
  - Header: `X-API-KEY`
  - Valor de ejemplo: `my-super-secret-key`
- **product-service**:
  - Tiene un `HandlerInterceptor` que valida la API key en todas las rutas `/api/**`.
  - Si el header falta o no coincide, responde `401` con JSON:
    ```json
    {
      "status": 401,
      "message": "Invalid or missing API key"
    }
    ```
- **inventory-service**:
  - No exige API key a los clientes externos (para facilitar las pruebas manuales).
  - Pero **sí** envía la API key cuando llama internamente al `product-service`, cumpliendo el requerimiento de autenticación entre servicios.
  - El `RestTemplate` agrega el header `X-API-KEY` automáticamente en cada llamada al `product-service`.

### Timeouts y reintentos

- El `RestTemplate` del `inventory-service` se configura con:
  - **Connect timeout**.
  - **Read timeout**.
- Implementé un mecanismo sencillo de **reintentos** en el `ProductClient`:
  - Reintenta la llamada al `product-service` un número limitado de veces ante errores de red/timeout antes de propagar la excepción.
  - Esto ayuda a evitar que un fallo temporal rompa inmediatamente el flujo de compra.

---

## Flujo de Git / Git Flow utilizado

Usé un flujo ligero inspirado en **Git Flow**. Las ramas principales que utilicé son:

- `main`: rama principal estable.
- `develop`: rama de desarrollo donde voy integrando las features.

Ramas de feature que fui creando sobre `develop`:

- `feature/product-service`  
  Implementación inicial del microservicio de productos.
- `feature/product-service-crudv1`  
  Primeras operaciones CRUD completas para productos.
- `feature/inventory-service`  
  Esqueleto e implementación base del microservicio de inventario.
- `feature/inventory-service-crudv1`  
  Endpoints de stock e inicio del flujo de compras.

Rama para la parte de seguridad e integración entre servicios:

- `security_integrations`  
  Configuración de API key, interceptores, cliente HTTP hacia `product-service`, timeouts y reintentos.

Flujo general:

1. Parto de `main` → creo `develop`.
2. Desde `develop` creo ramas `feature/*` o `security_integrations`.
3. Desarrollo y pruebo en cada rama de feature.
4. Hago merge de la feature a `develop` cuando está estable.
5. Desde `develop` se puede preparar un merge final a `main` para la entrega.

---

## Estructura del repositorio

```txt
spring-tech-test/
  db/
    init.sql                   # Script SQL inicial (origen Postgres, sirve como referencia del modelo)
  docker-compose.yml           # (opcional) definición de DB si se desea usar Docker
  product-service/
    pom.xml
    src/
      main/
        java/com/jdvergara/techtest/product/
          ProductServiceApplication.java
          domain/
            Product.java
          dto/
            ProductRequest.java
            ProductResponse.java
          repository/
            ProductRepository.java
          controller/
            ProductController.java
          config/
            ApiKeyProperties.java
            ApiKeyInterceptor.java
            WebMvcConfig.java
            CustomHealthIndicator.java
          exception/
            GlobalExceptionHandler.java
        resources/
          application.yaml
      test/
        ... (tests básicos / pendientes)
  inventory-service/
    pom.xml
    src/
      main/
        java/com/jdvergara/techtest/inventory_service/
          InventoryServiceApplication.java
          domain/
            InventoryItem.java
            Purchase.java
          dto/
            InventoryItemRequest.java
            InventoryItemResponse.java
            PurchaseRequest.java
            PurchaseResponse.java
            ProductDto.java
          repository/
            InventoryItemRepository.java
            PurchaseRepository.java
          client/
            ProductClient.java
          controller/
            InventoryController.java
          service/
            InventoryService.java
          config/
            RestTemplateConfig.java
            HealthCheckConfig.java
          exception/
            InsufficientStockException.java
            GlobalExceptionHandler.java
        resources/
          application.yaml
      test/
        ... (tests básicos / pendientes)
  README.md
