-- =========================================
-- Crear bases de datos para cada microservicio
-- =========================================

CREATE DATABASE IF NOT EXISTS product_db;
CREATE DATABASE IF NOT EXISTS inventory_db;

-- Otorgar permisos al usuario techuser
GRANT ALL PRIVILEGES ON product_db.* TO 'techuser'@'%';
GRANT ALL PRIVILEGES ON inventory_db.* TO 'techuser'@'%';
FLUSH PRIVILEGES;

-- =========================================
-- Esquema para product_db
-- =========================================

USE product_db;

CREATE TABLE products (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  TEXT,
    sku          VARCHAR(100) UNIQUE,
    price        DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    currency     CHAR(3) NOT NULL DEFAULT 'USD',
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_name ON products (name);

-- =========================================
-- Esquema para inventory_db
-- =========================================

USE inventory_db;

CREATE TABLE inventory_items (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id  BIGINT NOT NULL,
    quantity    INTEGER NOT NULL CHECK (quantity >= 0),
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX ux_inventory_items_product_id ON inventory_items (product_id);

CREATE TABLE purchases (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id   BIGINT NOT NULL,
    quantity     INTEGER NOT NULL CHECK (quantity > 0),
    unit_price   DECIMAL(10, 2) NOT NULL CHECK (unit_price >= 0),
    total_price  DECIMAL(12, 2) NOT NULL CHECK (total_price >= 0),
    purchased_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_purchases_product_id ON purchases (product_id);
