CREATE TABLE pagos (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       pedido_id BIGINT NOT NULL,
                       monto DOUBLE NOT NULL,
                       metodo_pago VARCHAR(30) NOT NULL,
                       estado VARCHAR(20) NOT NULL,
                       fecha_pago DATETIME NOT NULL,
                       referencia VARCHAR(100)
);
