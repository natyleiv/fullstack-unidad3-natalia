package cl.vincalia.proveedores_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProveedorDTO {
    private Long id;
    private String rut;
    private String razonSocial;
    private String rubro;
    private String emailContacto;
    private String telefono;
    private String direccion;
    private Boolean activo;
    private LocalDateTime fechaRegistro;
}