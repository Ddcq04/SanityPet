-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 26-05-2026 a las 14:35:07
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `sanitypet`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `citas`
--

CREATE TABLE `citas` (
  `id` bigint(20) NOT NULL,
  `fecha_hora` datetime NOT NULL,
  `motivo` varchar(255) DEFAULT NULL,
  `mascota_id` bigint(20) DEFAULT NULL,
  `descripcion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `citas`
--

INSERT INTO `citas` (`id`, `fecha_hora`, `motivo`, `mascota_id`, `descripcion`) VALUES
(2, '2026-02-15 10:00:00', 'Vacunación - 45€', 2, 'Dosis anual de rabia y pasaporte al día.'),
(3, '2026-03-01 11:30:00', 'Diagnóstico (Eco/Rx) - 60€', 3, 'Placa de tórax por tos leve. Sin hallazgos graves.'),
(4, '2026-03-22 17:00:00', 'Peluquería - 25€', 4, 'Corte higiénico y eliminación de nudos.'),
(5, '2026-04-05 12:00:00', 'Cirugía - Desde 120€', 5, 'Esterilización preventiva exitosa. Postoperatorio normal.'),
(6, '2026-04-18 09:00:00', 'Consulta General - 30€', 6, 'Chequeo bucal por sospecha de sarro.'),
(7, '2026-04-20 16:15:00', 'Vacunación - 45€', 7, 'Vacuna Heptavalente inoculada de forma limpia.'),
(8, '2026-05-02 10:45:00', 'Diagnóstico (Eco/Rx) - 60€', 8, 'Ecografía de control para revisar vías urinarias.'),
(9, '2026-05-10 18:30:00', 'Peluquería - 25€', 9, 'Lavado terapéutico para pieles atópicas.'),
(11, '2026-05-26 09:00:00', 'Consulta General - 30€', 10, 'Revisión por alergia cutánea estacional.'),
(12, '2026-05-26 10:30:00', 'Vacunación - 45€', 11, 'Recordatorio anual de la Leucemia Felina.'),
(13, '2026-05-26 12:00:00', 'Diagnóstico (Eco/Rx) - 60€', 12, 'Radiografía de control por torsión corregida.'),
(14, '2026-05-26 16:00:00', 'Peluquería - 25€', 13, 'Baño cosmético y corte a tijera de raza.'),
(15, '2026-05-26 17:30:00', 'Cirugía - Desde 120€', 14, 'Limpieza dental profunda bajo sedación suave.'),
(16, '2026-05-27 10:00:00', 'Consulta General - 30€', 15, 'Control periódico de azúcar en sangre.'),
(17, '2026-05-27 11:30:00', 'Vacunación - 45€', 16, 'Primera dosis de cachorro. Entrega de cartilla.'),
(18, '2026-05-28 09:15:00', 'Diagnóstico (Eco/Rx) - 60€', 17, 'Ecografía para confirmar sospecha de gestación.'),
(19, '2026-05-28 16:45:00', 'Peluquería - 25€', 18, 'Arreglo estival y corte de uñas.'),
(20, '2026-05-29 11:00:00', 'Cirugía - Desde 120€', 19, 'Extracción de pequeño quiste cutáneo benigno.'),
(21, '2026-05-29 13:30:00', 'Consulta General - 30€', 20, 'Evaluación digestiva por cambio de pienso.'),
(22, '2026-06-01 10:00:00', 'Consulta General - 30€', 21, 'Revisión ordinaria de oídos por exceso de cera.'),
(23, '2026-06-02 12:30:00', 'Vacunación - 45€', 22, 'Vacunación mixomatosis de conejo doméstico.'),
(24, '2026-06-03 09:00:00', 'Diagnóstico (Eco/Rx) - 60€', 23, 'Estudio radiológico de cadera por displasia.'),
(25, '2026-06-04 17:00:00', 'Peluquería - 25€', 24, 'Deslanado profundo por inicio de muda.'),
(26, '2026-06-08 10:15:00', 'Cirugía - Desde 120€', 25, 'Reparación de hernia umbilical simple.'),
(27, '2026-06-10 16:00:00', 'Consulta General - 30€', 26, 'Consulta de control de peso por obesidad severa.'),
(28, '2026-06-12 11:45:00', 'Vacunación - 45€', 27, 'Inoculación obligatoria trianual.'),
(29, '2026-06-15 09:30:00', 'Diagnóstico (Eco/Rx) - 60€', 28, 'Ecografía de riñón por insuficiencia leve.'),
(30, '2026-06-20 12:00:00', 'Peluquería - 25€', 29, 'Lavado clínico antipulgas preventivo.');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id` bigint(20) NOT NULL,
  `dni` varchar(9) NOT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `saldo` decimal(10,2) DEFAULT 0.00,
  `usuario_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id`, `dni`, `telefono`, `email`, `saldo`, `usuario_id`) VALUES
(2, '22222222B', '600222333', 'ana.silva@email.com', 50.00, 3),
(3, '33333333C', '600333444', 'juan.gomez@email.com', 0.00, 4),
(4, '44444444D', '600444555', 'laura.beltran@email.com', 8.25, 5),
(5, '55555555E', '600555666', 'miguel.benitez@email.com', 120.00, 6),
(6, '66666666F', '600666777', 'elena.rostova@email.com', 0.00, 7),
(7, '77777777G', '600777888', 'david.casals@email.com', 35.00, 8),
(8, '88888888H', '600888999', 'sofia.vega@email.com', 0.00, 9),
(9, '99999999I', '600999000', 'javier.ortiz@email.com', 4.50, 10),
(10, '12345678A', '611222333', 'carmen.nunez@email.com', 75.00, 11),
(11, '23456789B', '622333444', 'ale.sanz@email.com', 0.00, 12),
(12, '34567890C', '633444555', 'patricia.conde@email.com', 12.00, 13),
(13, '45678901D', '644555666', 'roberto.alva@email.com', 0.00, 14),
(14, '56789012E', '655666777', 'marta.sanchez@email.com', 110.50, 15),
(15, '67890123F', '666777888', 'nano.alonso@email.com', 0.00, 16),
(16, '78901234G', '677888999', 'lucia.la@email.com', 22.00, 17),
(17, '89012345H', '688999000', 'diego.maradona@email.com', 0.00, 18),
(18, '90123456I', '699000111', 'isabel.p@email.com', 5.00, 19),
(19, '01234567J', '600000111', 'andres.iniesta@email.com', 60.00, 20),
(20, '11223344K', '611111222', 'sergio.ramos@email.com', 0.00, 21),
(21, '22334455L', '622222333', 'sara.carbo@email.com', 14.20, 22),
(22, '33445566M', '633333444', 'iker.casillas@email.com', 0.00, 23),
(23, '44556677N', '644444555', 'gloria.estefan@email.com', 40.00, 24),
(24, '55667788O', '655555666', 'antonio.b@email.com', 0.00, 25),
(25, '66778899P', '666666777', 'penelope.cruz@email.com', 85.00, 26),
(26, '77889900Q', '677777888', 'javier.bardem@email.com', 0.00, 27),
(27, '88990011R', '688888999', 'rosalia.vila@email.com', 9.90, 28),
(28, '99001122S', '699999000', 'camilo.sesto@email.com', 0.00, 29),
(29, '10203040T', '600123456', 'joaquín.sabina@email.com', 130.00, 30),
(30, '50607080U', '600654321', 'julio.iglesias@email.com', 0.00, 31),
(31, '33378333C', '', '', 0.00, 32),
(40, '77889900X', '', '', 0.00, 51);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `compras`
--

CREATE TABLE `compras` (
  `id` bigint(20) NOT NULL,
  `cliente_id` bigint(20) DEFAULT NULL,
  `producto_id` bigint(20) DEFAULT NULL,
  `fecha_compra` datetime DEFAULT current_timestamp(),
  `precio_pagado` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mascotas`
--

CREATE TABLE `mascotas` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `especie` varchar(50) DEFAULT NULL,
  `raza` varchar(50) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `cliente_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `mascotas`
--

INSERT INTO `mascotas` (`id`, `nombre`, `especie`, `raza`, `fecha_nacimiento`, `cliente_id`) VALUES
(2, 'Luna', 'Gato', 'Siamés', '2021-09-20', 2),
(3, 'Rocky', 'Perro', 'Pastor Alemán', '2019-03-14', 3),
(4, 'Coco', 'Ave', 'Loro Amazonas', '2022-01-10', 4),
(5, 'Thor', 'Perro', 'Pitbull', '2018-11-30', 5),
(6, 'Mimi', 'Gato', 'Persa', '2023-06-05', 6),
(7, 'Max', 'Perro', 'Labrador', '2021-02-28', 7),
(8, 'Simba', 'Gato', 'Bengala', '2022-07-19', 8),
(9, 'Bella', 'Perro', 'Caniche', '2020-12-25', 9),
(10, 'Zeus', 'Perro', 'Rottweiler', '2017-08-04', 10),
(11, 'Mia', 'Gato', 'Sphynx', '2023-02-14', 11),
(12, 'Bruno', 'Perro', 'Boxer', '2019-10-10', 12),
(13, 'Lola', 'Perro', 'Chihuahua', '2022-04-17', 13),
(14, 'Félix', 'Gato', 'Común Europeo', '2016-05-20', 14),
(15, 'Lucas', 'Ave', 'Canario', '2024-01-01', 15),
(16, 'Kira', 'Perro', 'Husky Siberiano', '2021-11-11', 16),
(17, 'Nala', 'Gato', 'Angora', '2020-03-03', 17),
(18, 'Beto', 'Otro', 'Conejo Enano', '2023-08-24', 18),
(19, 'Bimba', 'Perro', 'Bulldog Francés', '2022-10-05', 19),
(20, 'Gaston', 'Gato', 'Maine Coon', '2019-07-07', 20),
(21, 'Pancho', 'Perro', 'Bodeguero', '2015-04-12', 21),
(22, 'Chispa', 'Otro', 'Cobaya', '2024-03-15', 22),
(23, 'Rocco', 'Perro', 'Doberman', '2018-02-19', 23),
(24, 'Frida', 'Perro', 'Carlino', '2021-06-16', 24),
(25, 'Platón', 'Otro', 'Tortuga de agua', '2010-05-05', 25),
(26, 'Oreo', 'Gato', 'Blanco y Negro', '2022-09-09', 26),
(27, 'Duque', 'Perro', 'Mastín', '2017-12-12', 27),
(28, 'Cleo', 'Gato', 'Ragdoll', '2023-11-01', 28),
(29, 'Paco', 'Ave', 'Agapornis', '2023-05-20', 29),
(30, 'Kiwi', 'Otro', 'Hurón', '2024-02-10', 30);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `precio` decimal(10,2) NOT NULL,
  `stock` int(11) DEFAULT 0,
  `categoria` varchar(50) DEFAULT NULL,
  `imagen_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id`, `nombre`, `descripcion`, `precio`, `stock`, `categoria`, `imagen_url`) VALUES
(1, 'Pienso Premium Perro 10kg', 'Alimento balanceado para perros adultos de raza mediana.', 45.99, 20, 'Alimentación', 'https://images.unsplash.com/photo-1589924691995-400dc9ecc119?w=500&auto=format&fit=crop&q=60'),
(2, 'Pienso Esterilizado Gato 3kg', 'Control de peso estricto para felinos domésticos de interior.', 24.50, 15, 'Alimentación', 'https://images.unsplash.com/photo-1569591159212-b02ea8a9f239?w=500&auto=format&fit=crop&q=60'),
(3, 'Comida Húmeda Buey Perro (X6)', 'Pack de 6 latas de sabroso buey en salsa gelatinosa.', 11.20, 30, 'Alimentación', 'https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=500&auto=format&fit=crop&q=60'),
(4, 'Snacks Dentales para Perros', 'Barritas masticables que previenen el sarro y mal aliento.', 5.95, 50, 'Alimentación', 'https://images.unsplash.com/photo-1548767797-d8c844163c4c?w=500&auto=format&fit=crop&q=60'),
(5, 'Pienso Cachorros Puppy 5kg', 'Fórmula con extra de calcio para el crecimiento de cachorros.', 29.99, 12, 'Alimentación', 'https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=500&auto=format&fit=crop&q=60'),
(6, 'Lata Salmón Gato Gourmet', 'Delicioso paté de salmón salvaje para los gatos más exigentes.', 1.95, 100, 'Alimentación', 'https://images.unsplash.com/photo-1535930891776-0c2dfb7fda1a?w=500&auto=format&fit=crop&q=60'),
(7, 'Mezcla Semillas Canarios 1kg', 'Alimento completo enriquecido con vitaminas para aves.', 4.50, 25, 'Alimentación', 'https://images.unsplash.com/photo-1607990283143-e81e7a2c93ab?w=500&auto=format&fit=crop&q=60'),
(8, 'Heno Premium Alfalfa 1kg', 'Heno fresco ideal para conejos, cobayas y pequeños roedores.', 6.20, 18, 'Alimentación', 'https://images.unsplash.com/photo-1516467508483-a7212febe31a?w=500&auto=format&fit=crop&q=60'),
(9, 'Champú Antiparasitario', 'Elimina pulgas, garrapatas y piojos protegiendo la barrera cutánea.', 12.80, 40, 'Higiene', 'https://images.unsplash.com/photo-1608248597481-496100c8c836?w=500&auto=format&fit=crop&q=60'),
(10, 'Arena de Gato Aglomerante 10L', 'Excelente control de olores y fácil de limpiar diariamente.', 14.90, 22, 'Higiene', 'https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=500&auto=format&fit=crop&q=60'),
(11, 'Limpiador de Oídos Otológico', 'Solución líquida para disolver el exceso de cera y suciedad.', 9.15, 30, 'Higiene', 'https://images.unsplash.com/photo-1628009368231-7bb7cfcb0def?w=500&auto=format&fit=crop&q=60'),
(12, 'Toallitas Higiénicas Pack 80', 'Toallitas húmedas sin alcohol para limpiar patas y ojos.', 4.25, 60, 'Higiene', 'https://images.unsplash.com/photo-1585624484084-74e2e283c79c?w=500&auto=format&fit=crop&q=60'),
(13, 'Cortauñas Ergonómico Perro', 'De acero inoxidable con tope de seguridad para evitar cortes.', 8.50, 15, 'Higiene', 'https://images.unsplash.com/photo-1516734212186-a967f81ad0d7?w=500&auto=format&fit=crop&q=60'),
(14, 'Spray Desenredante Pelaje', 'Suaviza el pelo largo facilitando el cepillado sin tirones.', 11.40, 25, 'Higiene', 'https://images.unsplash.com/photo-1527362950785-f487a7c1fe48?w=500&auto=format&fit=crop&q=60'),
(15, 'Pipetas Antiparasitarias (X4)', 'Protección total de 4 meses frente a mosquitos, pulgas y garrapatas.', 29.95, 50, 'Farmacia', 'https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=500&auto=format&fit=crop&q=60'),
(16, 'Collar Scalibor Grande', 'Collar repelente altamente eficaz contra el flebótomo de la Leishmania.', 32.10, 35, 'Farmacia', 'https://images.unsplash.com/photo-1544568100-847a948585b9?w=500&auto=format&fit=crop&q=60'),
(17, 'Suplemento Articular Condro', '60 comprimidos para reforzar las articulaciones de perros mayores.', 38.00, 14, 'Farmacia', 'https://images.unsplash.com/photo-1550572017-edd951b55104?w=500&auto=format&fit=crop&q=60'),
(18, 'Malta para Gatos Tubo 100g', 'Previene de forma eficaz la formación de bolas de pelo.', 7.50, 45, 'Farmacia', 'https://images.unsplash.com/photo-1597626122131-0ee21aacdee4?w=500&auto=format&fit=crop&q=60'),
(19, 'Crema Protectora Almohadillas', 'Hidrata y repara las almohadillas agrietadas por el frío o calor.', 13.25, 20, 'Farmacia', 'https://images.unsplash.com/photo-1607613009820-a29f7bb81c04?w=500&auto=format&fit=crop&q=60'),
(20, 'Antiséptico de Heridas Spray', 'Desinfectante de acción rápida que no escuece al animal.', 6.80, 40, 'Farmacia', 'https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?w=500&auto=format&fit=crop&q=60'),
(21, 'Rascador Gato Árbol', 'Estructura estable de tres niveles revestida con cuerda de sisal natural.', 55.00, 8, 'Juguetes', 'https://images.unsplash.com/photo-1545249390-6bdfa286032f?w=500&auto=format&fit=crop&q=60'),
(22, 'Pelota de Goma Irrompible', 'Goma maciza ultra resistente ideal para morder y lanzar.', 6.50, 40, 'Juguetes', 'https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=500&auto=format&fit=crop&q=60'),
(23, 'Mordedor de Cuerda Nudos', 'Hilos de algodón que ayudan a limpiar los dientes jugando.', 4.99, 35, 'Juguetes', 'https://images.unsplash.com/photo-1568640347023-a616a30bc3bd?w=500&auto=format&fit=crop&q=60'),
(24, 'Ratón de Juguete con Catnip', 'Pack de 3 ratoncitos rellenos de hierba gatera estimulante.', 3.50, 60, 'Juguetes', 'https://images.unsplash.com/photo-1513360309081-36f5e878f2d0?w=500&auto=format&fit=crop&q=60'),
(25, 'Juguete Dispensador Premios', 'Mantiene la mente del perro activa liberando comida al rodar.', 15.90, 18, 'Juguetes', 'https://images.unsplash.com/photo-1537151608828-ea2b117b6281?w=500&auto=format&fit=crop&q=60'),
(26, 'Cama Acolchada Confort L', 'Cama desenfundable y lavable para perros grandes o medianos.', 42.50, 10, 'Accesorios', 'https://images.unsplash.com/photo-1591946614720-90a587da4a36?w=500&auto=format&fit=crop&q=60'),
(27, 'Correa Extensible 5 metros', 'Cinta resistente con sistema de frenado cómodo con una mano.', 18.90, 25, 'Accesorios', 'https://images.unsplash.com/photo-1601758124540-52f84a22f300?w=500&auto=format&fit=crop&q=60'),
(28, 'Arnés Antitirones Ergonómico', 'Distribuye la presión de forma uniforme evitando ahogamientos.', 22.00, 15, 'Accesorios', 'https://images.unsplash.com/photo-1625316708582-7c38734be31d?w=500&auto=format&fit=crop&q=60'),
(29, 'Comedero Acero Inoxidable', 'Base de goma antideslizante y apto para lavavajillas.', 7.80, 30, 'Accesorios', 'https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=500&auto=format&fit=crop&q=60'),
(30, 'Transportín de Viaje Homologado', 'Rejillas de ventilación óptima y puerta de seguridad metálica.', 34.99, 12, 'Accesorios', 'https://images.unsplash.com/photo-1516734212186-a967f81ad0d7?w=500&auto=format&fit=crop&q=60');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` bigint(20) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` varchar(20) NOT NULL,
  `nombre_completo` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `username`, `password`, `rol`, `nombre_completo`) VALUES
(1, 'admin', 'admin', 'admin', 'admin'),
(2, '11111111A', '$2a$10$r.7gU6W9WbkW...', 'user', 'Carlos Mendoza Roldán'),
(3, '22222222B', '$2a$10$r.7gU6W9WbkW...', 'user', 'Ana María Silva Ortiz'),
(4, '33333333C', '$2a$10$r.7gU6W9WbkW...', 'user', 'Juan Pedro Gómez López'),
(5, '44444444D', '$2a$10$r.7gU6W9WbkW...', 'user', 'Laura Beltrán Torres'),
(6, '55555555E', '$2a$10$r.7gU6W9WbkW...', 'user', 'Miguel Ángel Benítez'),
(7, '66666666F', '$2a$10$r.7gU6W9WbkW...', 'user', 'Elena Rostova Sánchez'),
(8, '77777777G', '$2a$10$r.7gU6W9WbkW...', 'user', 'David Casals Prat'),
(9, '88888888H', '$2a$10$r.7gU6W9WbkW...', 'user', 'Sofía Vega Villanueva'),
(10, '99999999I', '$2a$10$r.7gU6W9WbkW...', 'user', 'Javier Ortiz Navarro'),
(11, '12345678A', '$2a$10$r.7gU6W9WbkW...', 'user', 'Carmen Núñez Delgado'),
(12, '23456789B', '$2a$10$r.7gU6W9WbkW...', 'user', 'Alejandro Sanz Peña'),
(13, '34567890C', '$2a$10$r.7gU6W9WbkW...', 'user', 'Patricia Conde Rey'),
(14, '45678901D', '$2a$10$r.7gU6W9WbkW...', 'user', 'Roberto Carlos Alva'),
(15, '56789012E', '$2a$10$r.7gU6W9WbkW...', 'user', 'Marta Sánchez Soler'),
(16, '67890123F', '$2a$10$r.7gU6W9WbkW...', 'user', 'Fernando Alonso Díaz'),
(17, '78901234G', '$2a$10$r.7gU6W9WbkW...', 'user', 'Lucía Lapiedra Gil'),
(18, '89012345H', '$2a$10$r.7gU6W9WbkW...', 'user', 'Diego Armando Franco'),
(19, '90123456I', '$2a$10$r.7gU6W9WbkW...', 'user', 'Isabel Pantoja Martín'),
(20, '01234567J', '$2a$10$r.7gU6W9WbkW...', 'user', 'Andrés Iniesta Luján'),
(21, '11223344K', '$2a$10$r.7gU6W9WbkW...', 'user', 'Sergio Ramos García'),
(22, '22334455L', '$2a$10$r.7gU6W9WbkW...', 'user', 'Sara Carbonero Arévalo'),
(23, '33445566M', '$2a$10$r.7gU6W9WbkW...', 'user', 'Iker Casillas Fernández'),
(24, '44556677N', '$2a$10$r.7gU6W9WbkW...', 'user', 'Gloria Estefan Fajardo'),
(25, '55667788O', '$2a$10$r.7gU6W9WbkW...', 'user', 'Antonio Banderas Galera'),
(26, '66778899P', '$2a$10$r.7gU6W9WbkW...', 'user', 'Penélope Cruz Sánchez'),
(27, '77889900Q', '$2a$10$r.7gU6W9WbkW...', 'user', 'Javier Bardem Encinas'),
(28, '88990011R', '$2a$10$r.7gU6W9WbkW...', 'user', 'Rosalía Vila Tobella'),
(29, '99001122S', '$2a$10$r.7gU6W9WbkW...', 'user', 'Camilo Sesto Blanes'),
(30, '10203040T', '$2a$10$r.7gU6W9WbkW...', 'user', 'Joaquín Sabina Martínez'),
(31, '50607080U', '$2a$10$r.7gU6W9WbkW...', 'user', 'Julio Iglesias de la Cueva'),
(32, '33378333C', '$2a$10$T4LKvNOZVoGyVUM.J3s4oOggw8eJqDSictfEH8HU1NVp/a.tVLQIC', 'admin', 'admin'),
(51, '77889900X', 'a', 'user', 'a');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `citas`
--
ALTER TABLE `citas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `mascota_id` (`mascota_id`);

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `dni` (`dni`),
  ADD UNIQUE KEY `usuario_id` (`usuario_id`);

--
-- Indices de la tabla `compras`
--
ALTER TABLE `compras`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cliente_id` (`cliente_id`),
  ADD KEY `producto_id` (`producto_id`);

--
-- Indices de la tabla `mascotas`
--
ALTER TABLE `mascotas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cliente_id` (`cliente_id`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `citas`
--
ALTER TABLE `citas`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT de la tabla `compras`
--
ALTER TABLE `compras`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `mascotas`
--
ALTER TABLE `mascotas`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `citas`
--
ALTER TABLE `citas`
  ADD CONSTRAINT `citas_ibfk_1` FOREIGN KEY (`mascota_id`) REFERENCES `mascotas` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD CONSTRAINT `clientes_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `compras`
--
ALTER TABLE `compras`
  ADD CONSTRAINT `compras_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `compras_ibfk_2` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`) ON DELETE SET NULL;

--
-- Filtros para la tabla `mascotas`
--
ALTER TABLE `mascotas`
  ADD CONSTRAINT `mascotas_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
