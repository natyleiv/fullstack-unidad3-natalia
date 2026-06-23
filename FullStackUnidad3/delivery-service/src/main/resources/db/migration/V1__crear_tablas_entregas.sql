CREATE TABLE entregas (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          pedido_id BIGINT NOT NULL,
                          direccion VARCHAR(200) NOT NULL,
                          estado VARCHAR(20) NOT NULL,
                          fecha_estimada DATETIME,
                          fecha_entrega_real DATETIME,
                          repartidor VARCHAR(100),
                          fecha_creacion DATETIME NOT NULL
);
