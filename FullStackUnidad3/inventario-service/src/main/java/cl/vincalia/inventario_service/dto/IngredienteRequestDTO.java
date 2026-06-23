package cl.vincalia.inventario_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class IngredienteRequestDTO {
    @NotBlank(message = "El nombre del ingrediente es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    @DecimalMax(value = "999999.99", message = "El precio no puede superar 999.999,99")
    private Double precio;

    private Boolean disponible;

    @Size(max = 50, message = "La unidad no puede superar los 50 caracteres")
    private String unidad;

    private Long productoId;
}