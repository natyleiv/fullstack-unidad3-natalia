package cl.vincalia.proveedores_service.mapper;

import cl.vincalia.proveedores_service.dto.ProveedorDTO;
import cl.vincalia.proveedores_service.dto.ProveedorRequestDTO;
import cl.vincalia.proveedores_service.model.Proveedor;
import org.springframework.stereotype.Component;

@Component
public class ProveedorMapper {

    public ProveedorDTO toDto(Proveedor entity) {
        if (entity == null) return null;
        ProveedorDTO dto = new ProveedorDTO();
        dto.setId(entity.getId());
        dto.setRut(entity.getRut());
        dto.setRazonSocial(entity.getRazonSocial());
        dto.setRubro(entity.getRubro());
        dto.setEmailContacto(entity.getEmailContacto());
        dto.setTelefono(entity.getTelefono());
        dto.setDireccion(entity.getDireccion());
        dto.setActivo(entity.getActivo());
        dto.setFechaRegistro(entity.getFechaRegistro());
        return dto;
    }

    public Proveedor toEntity(ProveedorRequestDTO dto) {
        if (dto == null) return null;
        Proveedor entity = new Proveedor();
        entity.setRut(dto.getRut());
        entity.setRazonSocial(dto.getRazonSocial());
        entity.setRubro(dto.getRubro());
        entity.setEmailContacto(dto.getEmailContacto());
        entity.setTelefono(dto.getTelefono());
        entity.setDireccion(dto.getDireccion());
        entity.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        return entity;
    }

    public void updateEntity(ProveedorRequestDTO dto, Proveedor entity) {
        if (dto.getRut() != null) entity.setRut(dto.getRut());
        if (dto.getRazonSocial() != null) entity.setRazonSocial(dto.getRazonSocial());
        if (dto.getRubro() != null) entity.setRubro(dto.getRubro());
        if (dto.getEmailContacto() != null) entity.setEmailContacto(dto.getEmailContacto());
        if (dto.getTelefono() != null) entity.setTelefono(dto.getTelefono());
        if (dto.getDireccion() != null) entity.setDireccion(dto.getDireccion());
        if (dto.getActivo() != null) entity.setActivo(dto.getActivo());
    }
}