INSERT INTO pagos (pedido_id, monto, metodo_pago, estado, fecha_pago, referencia) VALUES
                                                                                      (1, 4500, 'TARJETA_CREDITO', 'COMPLETADO', NOW(), 'TX-001'),
                                                                                      (2, 3200, 'EFECTIVO', 'COMPLETADO', NOW(), NULL),
                                                                                      (3, 5000, 'TRANSFERENCIA', 'PENDIENTE', NOW(), 'REF-123');