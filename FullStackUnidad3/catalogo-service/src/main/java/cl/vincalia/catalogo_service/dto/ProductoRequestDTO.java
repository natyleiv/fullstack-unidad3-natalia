package cl.vincalia.catalogo_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductoRequestDTO {
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    @DecimalMax(value = "999999.99", message = "El precio no puede superar 999.999,99")
    private Double precio;

    @NotBlank
    @Size(max = 50)
    private String categoria;

    private Boolean disponible;

    @Min(0)
    private Integer stock;
}
