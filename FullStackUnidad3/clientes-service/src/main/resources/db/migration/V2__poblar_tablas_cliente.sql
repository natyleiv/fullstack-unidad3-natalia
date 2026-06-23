-- Poblar tabla clientes con datos de ejemplo para una pyme de completos

INSERT INTO clientes (rut, nombre_completo, email, telefono, direccion, fecha_registro, activo) VALUES
                                                                                                    ('12345678-9', 'Juan Pérez González', 'juan.perez@email.com', '912345678', 'Av. Libertador Bernardo O´Higgins 1234, Santiago', NOW(), true),
                                                                                                    ('98765432-1', 'María Rodríguez Fernández', 'maria.rodriguez@email.com', '923456789', 'Calle Los Alerces 456, Providencia', NOW(), true),
                                                                                                    ('11122233-4', 'Carlos Soto Méndez', 'carlos.soto@email.com', '934567890', 'Av. Las Condes 789, Las Condes', NOW(), true),
                                                                                                    ('44455566-7', 'Ana María Torres', 'ana.torres@email.com', '945678901', 'Calle El Golf 101, Vitacura', NOW(), true),
                                                                                                    ('77788899-0', 'Luis Fernández Castillo', 'luis.fernandez@email.com', '956789012', 'Av. Pedro de Valdivia 202, Ñuñoa', NOW(), true),
                                                                                                    ('12121212-3', 'Patricia López Muñoz', 'patricia.lopez@email.com', '967890123', 'Calle Manuel Montt 303, Santiago', NOW(), true),
                                                                                                    ('34343434-5', 'Ricardo Díaz Paredes', 'ricardo.diaz@email.com', '978901234', 'Av. Providencia 404, Providencia', NOW(), true),
                                                                                                    ('56565656-7', 'Verónica Silva Herrera', 'veronica.silva@email.com', '989012345', 'Calle Nueva Los Leones 505, Las Condes', NOW(), true),
                                                                                                    ('78787878-9', 'Javier Reyes Espinoza', 'javier.reyes@email.com', '990123456', 'Av. Vitacura 606, Vitacura', NOW(), true),
                                                                                                    ('90909090-1', 'Paula Muñoz Villarroel', 'paula.munoz@email.com', '901234567', 'Calle Tobalaba 707, Ñuñoa', NOW(), true);

-- Incluir algunos clientes inactivos (para pruebas de reporte)
INSERT INTO clientes (rut, nombre_completo, email, telefono, direccion, fecha_registro, activo) VALUES
                                                                                                    ('13131313-1', 'Cliente Inactivo Uno', 'inactivo1@email.com', '911223344', 'Calle Falsa 123, Maipú', DATE_SUB(NOW(), INTERVAL 30 DAY), false),
                                                                                                    ('14141414-2', 'Cliente Inactivo Dos', 'inactivo2@email.com', '922334455', 'Av. Siempre Viva 456, La Florida', DATE_SUB(NOW(), INTERVAL 15 DAY), false);