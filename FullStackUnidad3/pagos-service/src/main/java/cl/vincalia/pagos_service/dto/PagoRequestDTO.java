package cl.vincalia.pagos_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PagoRequestDTO {
    @NotNull
    private Long pedidoId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor que 0")
    private Double monto;

    @NotBlank(message = "El método de pago es obligatorio")
    @Pattern(regexp = "^(EFECTIVO|TARJETA_CREDITO|TARJETA_DEBITO|TRANSFERENCIA|OTRO)$",
             message = "El método de pago debe ser: EFECTIVO, TARJETA_CREDITO, TARJETA_DEBITO, TRANSFERENCIA u OTRO")
    private String metodoPago;

    private String estado; // opcional, por defecto "PENDIENTE"

    private String referencia;
}