CREATE DATABASE sanitypet;
USE sanitypet;

-- 1. TABLA DE ACCESO (SEGURIDAD)
-- Aquí el 'username' será el DNI para el login.
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE, -- Aquí guardaremos el DNI (Login)
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL,             -- 'admin', 'user', 'vet'
    nombre_completo VARCHAR(100)
);

-- 2. TABLA DE NEGOCIO (CLIENTES)
-- Vinculamos cada cliente a un único usuario mediante UNIQUE en usuario_id.
CREATE TABLE clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(9) NOT NULL UNIQUE,       -- DNI para trámites/facturación
    telefono VARCHAR(15),
    email VARCHAR(100),
    saldo DECIMAL(10, 2) DEFAULT 0.00,
    usuario_id BIGINT UNIQUE,             -- Relación 1-a-1: Un login = Un cliente
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- 3. TABLA DE MASCOTAS
-- Añadimos borrado en cascada para que si se borra un cliente, se limpien sus mascotas.
CREATE TABLE mascotas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    especie VARCHAR(50),
    raza VARCHAR(50),
    fecha_nacimiento DATE,
    cliente_id BIGINT,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
);

-- 4. TABLA DE PRODUCTOS (TIENDA/ALMACÉN)
CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0,
    categoria VARCHAR(50)
);

-- 5. TABLA DE CITAS MÉDICAS
CREATE TABLE citas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora DATETIME NOT NULL,
    motivo VARCHAR(255),
    mascota_id BIGINT,
    FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON DELETE CASCADE
);

-- 6. TABLA DE COMPRAS (HISTORIAL)
CREATE TABLE compras (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT,
    producto_id BIGINT,
    fecha_compra DATETIME DEFAULT CURRENT_TIMESTAMP,
    precio_pagado DECIMAL(10, 2),
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE SET NULL,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE SET NULL
);