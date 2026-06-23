package cl.vincalia.cupones_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CuponDTO {
    private Long id;
    private String codigo;
    private Double descuento;
    private LocalDateTime fechaVencimiento;
    private Boolean usado;
    private LocalDateTime fechaCreacion;
    private Boolean activo;
}