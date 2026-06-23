package cl.vincalia.pagos_service.mapper;

import cl.vincalia.pagos_service.dto.PagoDTO;
import cl.vincalia.pagos_service.dto.PagoRequestDTO;
import cl.vincalia.pagos_service.model.Pago;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {

    public PagoDTO toDto(Pago entity) {
        if (entity == null) return null;
        PagoDTO dto = new PagoDTO();
        dto.setId(entity.getId());
        dto.setPedidoId(entity.getPedidoId());
        dto.setMonto(entity.getMonto());
        dto.setMetodoPago(entity.getMetodoPago());
        dto.setEstado(entity.getEstado());
        dto.setFechaPago(entity.getFechaPago());
        dto.setReferencia(entity.getReferencia());
        return dto;
    }

    public Pago toEntity(PagoRequestDTO dto) {
        if (dto == null) return null;
        Pago entity = new Pago();
        entity.setPedidoId(dto.getPedidoId());
        entity.setMonto(dto.getMonto());
        entity.setMetodoPago(dto.getMetodoPago());
        entity.setEstado(dto.getEstado() != null ? dto.getEstado() : "PENDIENTE");
        entity.setReferencia(dto.getReferencia());
        return entity;
    }

    public void updateEntity(PagoRequestDTO dto, Pago entity) {
        if (dto.getPedidoId() != null) entity.setPedidoId(dto.getPedidoId());
        if (dto.getMonto() != null) entity.setMonto(dto.getMonto());
        if (dto.getMetodoPago() != null) entity.setMetodoPago(dto.getMetodoPago());
        if (dto.getEstado() != null) entity.setEstado(dto.getEstado());
        if (dto.getReferencia() != null) entity.setReferencia(dto.getReferencia());
    }
}