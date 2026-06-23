package cl.vincalia.pagos_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PagoDTO {
    private Long id;
    private Long pedidoId;
    private Double monto;
    private String metodoPago;
    private String estado;
    private LocalDateTime fechaPago;
    private String referencia;
}