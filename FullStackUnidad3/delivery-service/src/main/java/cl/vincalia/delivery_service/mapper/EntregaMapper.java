package cl.vincalia.delivery_service.mapper;

import cl.vincalia.delivery_service.dto.EntregaDTO;
import cl.vincalia.delivery_service.dto.EntregaRequestDTO;
import cl.vincalia.delivery_service.model.Entrega;
import org.springframework.stereotype.Component;

@Component
public class EntregaMapper {

    public EntregaDTO toDto(Entrega entity) {
        if (entity == null) return null;
        EntregaDTO dto = new EntregaDTO();
        dto.setId(entity.getId());
        dto.setPedidoId(entity.getPedidoId());
        dto.setDireccion(entity.getDireccion());
        dto.setEstado(entity.getEstado());
        dto.setFechaEstimada(entity.getFechaEstimada());
        dto.setFechaEntregaReal(entity.getFechaEntregaReal());
        dto.setRepartidor(entity.getRepartidor());
        dto.setFechaCreacion(entity.getFechaCreacion());
        return dto;
    }

    public Entrega toEntity(EntregaRequestDTO dto) {
        if (dto == null) return null;
        Entrega entity = new Entrega();
        entity.setPedidoId(dto.getPedidoId());
        entity.setDireccion(dto.getDireccion());
        entity.setEstado(dto.getEstado() != null ? dto.getEstado() : "PENDIENTE");
        entity.setFechaEstimada(dto.getFechaEstimada());
        entity.setRepartidor(dto.getRepartidor());
        return entity;
    }

    public void updateEntity(EntregaRequestDTO dto, Entrega entity) {
        if (dto.getDireccion() != null) entity.setDireccion(dto.getDireccion());
        if (dto.getEstado() != null) entity.setEstado(dto.getEstado());
        if (dto.getFechaEstimada() != null) entity.setFechaEstimada(dto.getFechaEstimada());
        if (dto.getRepartidor() != null) entity.setRepartidor(dto.getRepartidor());
    }
}