package cl.vincalia.cupones_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CuponRequestDTO {
    @NotBlank(message = "El código del cupón es obligatorio")
    @Size(min = 3, max = 20, message = "El código debe tener entre 3 y 20 caracteres")
    private String codigo;

    @NotNull(message = "El descuento es obligatorio")
    @DecimalMin(value = "0.0", message = "El descuento no puede ser negativo")
    @DecimalMax(value = "100.0", message = "El descuento no puede superar el 100%")
    private Double descuento;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @FutureOrPresent(message = "La fecha de vencimiento no puede ser pasada")
    private LocalDateTime fechaVencimiento;

    private Boolean usado;
    private Boolean activo;
}