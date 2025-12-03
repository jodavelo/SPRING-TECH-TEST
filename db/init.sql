-- =========================================
-- Crear bases de datos para cada microservicio
-- =========================================

CREATE DATABASE product_db;
CREATE DATABASE inventory_db;

-- Otorgar permisos al usuario techuser
GRANT ALL PRIVILEGES ON DATABASE product_db TO techuser;
GRANT ALL PRIVILEGES ON DATABASE inventory_db TO techuser;

-- =========================================
-- Esquema para product_db
-- =========================================

\connect product_db;

-- Otorgar permisos en el esquema public
GRANT ALL ON SCHEMA public TO techuser;

CREATE TABLE products (
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  TEXT,
    sku          VARCHAR(100) UNIQUE,
    price        NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
    currency     CHAR(3) NOT NULL DEFAULT 'USD',
    created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_products_name ON products (name);

-- Otorgar permisos sobre todas las tablas y secuencias
ALTER TABLE products OWNER TO techuser;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO techuser;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO techuser;

-- =========================================
-- Esquema para inventory_db
-- =========================================

\connect inventory_db;

-- Otorgar permisos en el esquema public
GRANT ALL ON SCHEMA public TO techuser;

CREATE TABLE inventory_items (
    id          BIGSERIAL PRIMARY KEY,
    product_id  BIGINT NOT NULL,
    quantity    INTEGER NOT NULL CHECK (quantity >= 0),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX ux_inventory_items_product_id
    ON inventory_items (product_id);

CREATE TABLE purchases (
    id           BIGSERIAL PRIMARY KEY,
    product_id   BIGINT NOT NULL,
    quantity     INTEGER NOT NULL CHECK (quantity > 0),
    unit_price   NUMERIC(10, 2) NOT NULL CHECK (unit_price >= 0),
    total_price  NUMERIC(12, 2) NOT NULL CHECK (total_price >= 0),
    purchased_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_purchases_product_id
    ON purchases (product_id);

-- Otorgar permisos sobre todas las tablas y secuencias
ALTER TABLE inventory_items OWNER TO techuser;
ALTER TABLE purchases OWNER TO techuser;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO techuser;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO techuser;
