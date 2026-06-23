CREATE TABLE proveedores (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             rut VARCHAR(12) NOT NULL UNIQUE,
                             razon_social VARCHAR(100) NOT NULL,
                             rubro VARCHAR(50) NOT NULL,
                             email_contacto VARCHAR(100) NOT NULL UNIQUE,
                             telefono VARCHAR(9) NOT NULL,
                             direccion VARCHAR(200),
                             activo BOOLEAN DEFAULT TRUE,
                             fecha_registro DATETIME NOT NULL
);
