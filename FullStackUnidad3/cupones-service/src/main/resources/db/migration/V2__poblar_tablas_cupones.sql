INSERT INTO cupones (codigo, descuento, fecha_vencimiento, usado, fecha_creacion, activo) VALUES
                                                                                              ('DESC10', 10.0, DATE_ADD(NOW(), INTERVAL 30 DAY), false, NOW(), true),
                                                                                              ('DESC20', 20.0, DATE_ADD(NOW(), INTERVAL 15 DAY), false, NOW(), true),
                                                                                              ('OFERTA30', 30.0, DATE_ADD(NOW(), INTERVAL 7 DAY), false, NOW(), true),
                                                                                              ('BIENVENIDA15', 15.0, DATE_ADD(NOW(), INTERVAL 60 DAY), false, NOW(), true),
                                                                                              ('BLACKFRIDAY', 50.0, DATE_ADD(NOW(), INTERVAL 365 DAY), false, NOW(), true);