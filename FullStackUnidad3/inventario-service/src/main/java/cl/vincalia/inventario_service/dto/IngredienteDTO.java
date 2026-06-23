package cl.vincalia.inventario_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IngredienteDTO {
    private Long id;
    private String nombre;
    private Integer stock;
    private Double precio;
    private Boolean disponible;
    private LocalDateTime fechaCreacion;
    private String unidad;
}
