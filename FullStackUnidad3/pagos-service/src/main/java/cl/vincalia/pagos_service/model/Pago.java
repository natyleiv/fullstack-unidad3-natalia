package cl.vincalia.pagos_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del pedido es obligatorio")
    private Long pedidoId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    @Column(columnDefinition = "DOUBLE")
    private Double monto;

    @NotBlank(message = "El método de pago es obligatorio")
    @Pattern(regexp = "^(EFECTIVO|TARJETA_CREDITO|TARJETA_DEBITO|TRANSFERENCIA|OTRO)$",
            message = "Método de pago no válido")
    private String metodoPago;

    @NotBlank(message = "El estado del pago es obligatorio")
    @Pattern(regexp = "^(PENDIENTE|COMPLETADO|FALLIDO|REEMBOLSADO)$",
            message = "Estado no válido")
    private String estado = "PENDIENTE";

    @Column(name = "fecha_pago", updatable = false)
    private LocalDateTime fechaPago = LocalDateTime.now();

    private String referencia; // código de transacción, comprobante, etc.
}