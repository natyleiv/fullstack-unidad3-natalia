package cl.vincalia.proveedores_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProveedorRequestDTO {
    @NotBlank
    @Pattern(regexp = "^[0-9]{7,8}-[0-9Kk]$")
    private String rut;

    @NotBlank
    @Size(min = 3, max = 100)
    private String razonSocial;

    @NotBlank
    @Size(max = 50)
    private String rubro;

    @NotBlank
    @Email
    private String emailContacto;

    @NotBlank
    @Pattern(regexp = "^[0-9]{9}$")
    private String telefono;

    private String direccion;

    private Boolean activo;
}