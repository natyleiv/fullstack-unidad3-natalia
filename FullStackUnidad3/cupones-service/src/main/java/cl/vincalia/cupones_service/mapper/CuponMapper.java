package cl.vincalia.cupones_service.mapper;

import cl.vincalia.cupones_service.dto.CuponDTO;
import cl.vincalia.cupones_service.dto.CuponRequestDTO;
import cl.vincalia.cupones_service.model.Cupon;
import org.springframework.stereotype.Component;

@Component
public class CuponMapper {

    public CuponDTO toDto(Cupon entity) {
        if (entity == null) return null;
        CuponDTO dto = new CuponDTO();
        dto.setId(entity.getId());
        dto.setCodigo(entity.getCodigo());
        dto.setDescuento(entity.getDescuento());
        dto.setFechaVencimiento(entity.getFechaVencimiento());
        dto.setUsado(entity.getUsado());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setActivo(entity.getActivo());
        return dto;
    }

    public Cupon toEntity(CuponRequestDTO dto) {
        if (dto == null) return null;
        Cupon entity = new Cupon();
        entity.setCodigo(dto.getCodigo());
        entity.setDescuento(dto.getDescuento());
        entity.setFechaVencimiento(dto.getFechaVencimiento());
        entity.setUsado(dto.getUsado() != null ? dto.getUsado() : false);
        entity.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        return entity;
    }

    public void updateEntity(CuponRequestDTO dto, Cupon entity) {
        if (dto.getCodigo() != null) entity.setCodigo(dto.getCodigo());
        if (dto.getDescuento() != null) entity.setDescuento(dto.getDescuento());
        if (dto.getFechaVencimiento() != null) entity.setFechaVencimiento(dto.getFechaVencimiento());
        if (dto.getUsado() != null) entity.setUsado(dto.getUsado());
        if (dto.getActivo() != null) entity.setActivo(dto.getActivo());
    }
}