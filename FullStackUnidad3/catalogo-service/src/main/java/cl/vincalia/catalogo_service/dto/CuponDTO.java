package cl.vincalia.catalogo_service.dto;

import lombok.Data;

@Data
public class CuponDTO {
    private String codigo;
    private Double descuento;
    private Boolean valido;
}
