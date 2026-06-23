CREATE TABLE ingredientes (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              nombre VARCHAR(100) NOT NULL UNIQUE,
                              stock INT NOT NULL,
                              precio DOUBLE NOT NULL,
                              disponible BOOLEAN DEFAULT TRUE,
                              fecha_creacion DATETIME NOT NULL,
                              unidad VARCHAR(50)
);
