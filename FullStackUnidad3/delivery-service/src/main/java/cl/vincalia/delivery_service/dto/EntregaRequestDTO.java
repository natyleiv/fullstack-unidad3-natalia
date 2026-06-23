package cl.vincalia.delivery_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EntregaRequestDTO {
    @NotNull(message = "El id del pedido es obligatorio")
    private Long pedidoId;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres")
    private String direccion;

    private String estado; // opcional

    private LocalDateTime fechaEstimada;

    private String repartidor;
}