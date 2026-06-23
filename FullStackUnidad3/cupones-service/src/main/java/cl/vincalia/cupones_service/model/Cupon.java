package cl.vincalia.cupones_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cupones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El código del cupón es obligatorio")
    @Size(min = 3, max = 20, message = "El código debe tener entre 3 y 20 caracteres")
    @Column(unique = true, nullable = false)
    private String codigo;

    @NotNull(message = "El porcentaje de descuento es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El descuento no puede ser negativo")
    @DecimalMax(value = "100.0", inclusive = true, message = "El descuento no puede superar el 100%")
    @Column(columnDefinition = "DOUBLE")
    private Double descuento; // porcentaje (0-100)

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @FutureOrPresent(message = "La fecha de vencimiento debe ser hoy o posterior")
    private LocalDateTime fechaVencimiento;

    private Boolean usado = false;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private Boolean activo = true;
}