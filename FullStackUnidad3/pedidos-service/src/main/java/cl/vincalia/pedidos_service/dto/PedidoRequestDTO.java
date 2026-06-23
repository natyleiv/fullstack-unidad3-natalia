package cl.vincalia.pedidos_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class PedidoRequestDTO {
    @NotNull
    private Long clienteId;

    @PositiveOrZero
    private Double total;

    private String observaciones;
}