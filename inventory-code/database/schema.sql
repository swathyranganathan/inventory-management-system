-- ============================================================
-- Inventory Management System — Database Schema
-- MySQL 8.0+
-- ============================================================

CREATE DATABASE IF NOT EXISTS inventory_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE inventory_db;

-- ── Products table ───────────────────────────────────────────
CREATE TABLE IF NOT EXISTS products (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(200)    NOT NULL,
    category    VARCHAR(100)    NOT NULL,
    price       DECIMAL(12, 2)  NOT NULL,
    quantity    INT             NOT NULL DEFAULT 0,
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT chk_price    CHECK (price >= 0),
    CONSTRAINT chk_quantity CHECK (quantity >= 0),

    INDEX idx_category   (category),
    INDEX idx_quantity   (quantity),
    INDEX idx_created_at (created_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ── Seed data ────────────────────────────────────────────────
INSERT INTO products (name, category, price, quantity) VALUES
('Wireless Keyboard',       'Electronics',  49.99,  8),
('USB-C Hub 7-Port',        'Electronics',  34.99,  15),
('27" Monitor Stand',       'Electronics',  89.99,  25),
('Mechanical Pencil Set',   'Stationery',   12.50,  5),
('A4 Notebook (Pack of 5)', 'Stationery',   8.99,   50),
('Ballpoint Pens (Box 20)', 'Stationery',   6.49,   10),
('Office Chair Lumbar Pad', 'Furniture',    39.99,  18),
('Standing Desk Mat',       'Furniture',    59.99,  30),
('Filing Cabinet (2 Draw)', 'Furniture',    129.99, 7),
('Whiteboard Markers Set',  'Stationery',   9.99,   3),
('Laptop Stand Aluminum',   'Electronics',  29.99,  22),
('Wireless Mouse',          'Electronics',  24.99,  12),
('Desk Lamp LED',           'Furniture',    45.00,  9),
('Stapler Heavy Duty',      'Stationery',   14.99,  20),
('HDMI Cable 2m',           'Electronics',  11.99,  40);
