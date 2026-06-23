package cl.vincalia.pedidos_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PedidoDTO {
    private Long id;
    private Long clienteId;
    private Double total;
    private LocalDateTime fechaCreacion;
    private String observaciones;
}
