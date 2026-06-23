package cl.vincalia.proveedores_service.dto;

import lombok.Data;

@Data
public class IngredienteDTO {
    private Long id;
    private String nombre;
    private Integer stock;
    private Double precioUnitario;
}