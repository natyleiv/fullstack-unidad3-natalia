CREATE TABLE cupones (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         codigo VARCHAR(20) NOT NULL UNIQUE,
                         descuento DOUBLE NOT NULL,
                         fecha_vencimiento DATETIME NOT NULL,
                         usado BOOLEAN DEFAULT FALSE,
                         fecha_creacion DATETIME NOT NULL,
                         activo BOOLEAN DEFAULT TRUE
);
