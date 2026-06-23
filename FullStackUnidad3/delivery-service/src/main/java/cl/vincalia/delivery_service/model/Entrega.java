package cl.vincalia.delivery_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "entregas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del pedido es obligatorio")
    private Long pedidoId;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    @Size(max = 200)
    private String direccion;

    @NotBlank(message = "El estado de la entrega es obligatorio")
    @Pattern(regexp = "^(PENDIENTE|EN_CAMINO|ENTREGADO|CANCELADO)$",
            message = "Estado inválido: debe ser PENDIENTE, EN_CAMINO, ENTREGADO o CANCELADO")
    private String estado = "PENDIENTE";

    @Column(name = "fecha_estimada")
    private LocalDateTime fechaEstimada;

    @Column(name = "fecha_entrega_real")
    private LocalDateTime fechaEntregaReal;

    private String repartidor; // nombre o ID del repartidor

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
