CREATE TABLE productos (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL UNIQUE,
                           descripcion VARCHAR(500),
                           precio DOUBLE NOT NULL,
                           categoria VARCHAR(50) NOT NULL,
                           disponible BOOLEAN DEFAULT TRUE,
                           fecha_creacion DATETIME NOT NULL,
                           stock INT DEFAULT 0
);
