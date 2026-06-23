package cl.vincalia.pagos_service.dto;

import lombok.Data;

@Data
public class PedidoDTO {
    private Long id;
    private Double total;
    private String estado; // podría ser "PENDIENTE", "PAGADO", etc.
}