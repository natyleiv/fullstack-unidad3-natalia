package cl.vincalia.proveedores_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9Kk]$", message = "Formato de RUT inválido (ej: 12345678-9)")
    @Column(unique = true, nullable = false)
    private String rut;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(min = 3, max = 100)
    private String razonSocial;

    @NotBlank(message = "El rubro es obligatorio")
    @Size(max = 50)
    private String rubro; // ej: "Carnes", "Panadería", "Bebidas"

    @NotBlank(message = "El email de contacto es obligatorio")
    @Email
    private String emailContacto;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{9}$", message = "Teléfono debe tener 9 dígitos")
    private String telefono;

    private String direccion;

    private Boolean activo = true;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}