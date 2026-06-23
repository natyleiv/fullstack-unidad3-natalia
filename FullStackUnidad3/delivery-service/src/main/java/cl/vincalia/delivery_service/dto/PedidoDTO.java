package cl.vincalia.delivery_service.dto;

import lombok.Data;

@Data
public class PedidoDTO {
    private Long id;
    private Long clienteId;
    private Double total;
    private String estado; // PENDIENTE, PAGADO, etc.
}