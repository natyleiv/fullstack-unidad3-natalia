package cl.vincalia.clientes_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // NUEVO CAMPO RUT
    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9Kk]$", message = "Formato de RUT inválido (ej: 12345678-9)")
    @Column(unique = true, nullable = false)
    private String rut;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false)
    private String nombreCompleto;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe tener exactamente 9 dígitos")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200)
    private String direccion;

    @NotNull(message = "La fecha de registro no puede ser nula")
    @PastOrPresent(message = "La fecha de registro no puede ser futura")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    private Boolean activo = true;
}
