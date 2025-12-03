# Prueba técnica Backend – Microservicios con Spring Boot

Este repositorio contiene mi solución a la prueba técnica de backend, implementada con una arquitectura basada en **microservicios** usando **Spring Boot** y **PostgreSQL** sobre Docker.

La idea general es tener al menos dos microservicios:

- `product-service`: gestión del catálogo de productos.
- `inventory-service`: gestión de inventario y flujo de compra (se implementa después).

Por ahora, el foco está en dejar preparado el entorno, la base de datos y el primer microservicio (`product-service`).

---

## Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3.4.x**
  - Spring Web
  - Spring Data JPA
  - Validation
- **PostgreSQL 16** (en Docker)
- **Docker / Docker Compose**
- **VS Code** como entorno de desarrollo (Windows 11)

---

## Decisiones de arquitectura (inicio)

- Utilizo **microservicios**: cada contexto tiene su propia aplicación Spring Boot y su propia base de datos lógica.
- Para esta prueba opté por un enfoque **solo SQL** con PostgreSQL.  
  Incluir NoSQL no aporta demasiado en este caso y solo añade complejidad innecesaria para el alcance de la prueba.
- En la base de datos hay:
  - Una BD para productos: `product_db`
  - Una BD para inventario y compras: `inventory_db`
- Más adelante, cada microservicio se conectará únicamente a su propia BD:
  - `product-service` → `product_db`
  - `inventory-service` → `inventory_db`
- La comunicación entre microservicios será vía HTTP, utilizando JSON (formato JSON:API) y autenticación mediante API key.

---

## Estructura del proyecto

```txt
spring-tech-test/
  db/
    init.sql              # Script SQL de inicialización de las bases de datos
  docker-compose.yml      # Definición de Postgres (y después de los servicios)
  product-service/        # Microservicio de productos (Spring Boot)
    pom.xml
    mvnw / mvnw.cmd
    src/
      main/
        java/...
        resources/
      test/...
  inventory-service/      # (pendiente de crear)
