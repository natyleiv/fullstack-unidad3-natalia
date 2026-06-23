package cl.vincalia.clientes_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClienteDTO {
    private Long id;

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9Kk]$", message = "El RUT debe tener formato válido (ej: 12345678-9 o 1234567-K)")
    private String rut;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombreCompleto;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido (ej: usuario@dominio.com)")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe contener exactamente 9 dígitos numéricos (ej: 912345678)")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200)
    private String direccion;

    private LocalDateTime fechaRegistro;
    private Boolean activo;
}