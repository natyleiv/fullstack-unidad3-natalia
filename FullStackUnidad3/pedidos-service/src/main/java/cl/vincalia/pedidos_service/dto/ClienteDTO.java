package cl.vincalia.pedidos_service.dto;

import lombok.Data;

@Data
public class ClienteDTO {
    private Long id;
    private String rut;
    private String nombreCompleto;
    private Boolean activo;
}
