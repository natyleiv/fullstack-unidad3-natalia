package cl.vincalia.delivery_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EntregaDTO {
    private Long id;
    private Long pedidoId;
    private String direccion;
    private String estado;
    private LocalDateTime fechaEstimada;
    private LocalDateTime fechaEntregaReal;
    private String repartidor;
    private LocalDateTime fechaCreacion;
}