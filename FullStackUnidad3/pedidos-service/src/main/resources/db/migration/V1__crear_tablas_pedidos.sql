CREATE TABLE pedidos (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         cliente_id BIGINT NOT NULL,
                         total DOUBLE DEFAULT 0.00,
                         fecha_creacion DATETIME NOT NULL,
                         observaciones VARCHAR(255)
);

