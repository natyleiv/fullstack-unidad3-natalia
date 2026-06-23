CREATE TABLE clientes (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          rut VARCHAR(12) NOT NULL UNIQUE,
                          nombre_completo VARCHAR(100) NOT NULL,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          telefono VARCHAR(9) NOT NULL,
                          direccion VARCHAR(200) NOT NULL,
                          fecha_registro DATETIME NOT NULL,
                          activo BOOLEAN DEFAULT TRUE
);