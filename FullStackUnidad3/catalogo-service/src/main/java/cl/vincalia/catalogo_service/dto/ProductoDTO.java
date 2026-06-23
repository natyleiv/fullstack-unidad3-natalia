package cl.vincalia.catalogo_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String categoria;
    private Boolean disponible;
    private LocalDateTime fechaCreacion;
    private Integer stock;
}