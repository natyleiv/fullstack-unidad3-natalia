package cl.vincalia.clientes_service.mapper;

import cl.vincalia.clientes_service.dto.ClienteDTO;
import cl.vincalia.clientes_service.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    // Convierte de Entidad a DTO
    public ClienteDTO toDto(Cliente cliente) {
        if (cliente == null) return null;
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setRut(cliente.getRut());
        dto.setNombreCompleto(cliente.getNombreCompleto());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());
        dto.setDireccion(cliente.getDireccion());
        dto.setFechaRegistro(cliente.getFechaRegistro());
        dto.setActivo(cliente.getActivo());
        return dto;
    }

    // Convierte de DTO a Entidad
    public Cliente toEntity(ClienteDTO dto) {
        if (dto == null) return null;
        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        cliente.setRut(dto.getRut());
        cliente.setNombreCompleto(dto.getNombreCompleto());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
        cliente.setFechaRegistro(dto.getFechaRegistro());
        cliente.setActivo(dto.getActivo());
        return cliente;
    }

    // Actualiza una entidad existente con datos del DTO
    public void updateEntityFromDto(ClienteDTO dto, Cliente entity) {
        if (dto == null || entity == null) return;
        entity.setRut(dto.getRut());
        entity.setNombreCompleto(dto.getNombreCompleto());
        entity.setEmail(dto.getEmail());
        entity.setTelefono(dto.getTelefono());
        entity.setDireccion(dto.getDireccion());
        // No actualizamos fechaRegistro ni activo a menos que vengan explícitamente
        if (dto.getFechaRegistro() != null) entity.setFechaRegistro(dto.getFechaRegistro());
        if (dto.getActivo() != null) entity.setActivo(dto.getActivo());
    }
}