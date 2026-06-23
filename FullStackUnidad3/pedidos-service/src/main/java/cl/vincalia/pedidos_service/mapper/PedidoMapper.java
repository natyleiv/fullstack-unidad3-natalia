package cl.vincalia.pedidos_service.mapper;

import cl.vincalia.pedidos_service.dto.PedidoDTO;
import cl.vincalia.pedidos_service.dto.PedidoRequestDTO;
import cl.vincalia.pedidos_service.model.Pedido;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper {

    public PedidoDTO toDto(Pedido pedido) {
        if (pedido == null) return null;
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setClienteId(pedido.getClienteId());
        dto.setTotal(pedido.getTotal());
        dto.setFechaCreacion(pedido.getFechaCreacion());
        dto.setObservaciones(pedido.getObservaciones());
        return dto;
    }

    public Pedido toEntity(PedidoRequestDTO dto) {
        if (dto == null) return null;
        Pedido pedido = new Pedido();
        pedido.setClienteId(dto.getClienteId());
        pedido.setTotal(dto.getTotal() != null ? dto.getTotal() : 0.0);
        pedido.setObservaciones(dto.getObservaciones());
        return pedido;
    }

    public void updateEntity(PedidoRequestDTO dto, Pedido pedido) {
        if (dto.getTotal() != null) pedido.setTotal(dto.getTotal());
        if (dto.getObservaciones() != null) pedido.setObservaciones(dto.getObservaciones());
    }
}
