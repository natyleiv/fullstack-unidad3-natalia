package cl.vincalia.pedidos_service.service;

import cl.vincalia.pedidos_service.clients.ClienteFeignClient;
import cl.vincalia.pedidos_service.dto.ClienteDTO;
import cl.vincalia.pedidos_service.dto.PedidoDTO;
import cl.vincalia.pedidos_service.dto.PedidoRequestDTO;
import cl.vincalia.pedidos_service.exception.BusinessException;
import cl.vincalia.pedidos_service.exception.ResourceNotFoundException;
import cl.vincalia.pedidos_service.mapper.PedidoMapper;
import cl.vincalia.pedidos_service.model.Pedido;
import cl.vincalia.pedidos_service.repository.PedidoRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository repository;
    private final PedidoMapper mapper;
    private final ClienteFeignClient clienteClient;

    @Transactional
    public PedidoDTO crearPedido(PedidoRequestDTO request) {
        try {
            ClienteDTO cliente = clienteClient.obtenerClientePorId(request.getClienteId());
            if (cliente == null) {
                throw new ResourceNotFoundException("No existe cliente con id: " + request.getClienteId());
            }
            if (!cliente.getActivo()) {
                throw new BusinessException("El cliente con id " + request.getClienteId() + " está inactivo y no puede realizar pedidos");
            }
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("No existe cliente con id: " + request.getClienteId());
        } catch (FeignException e) {
            throw new BusinessException("Error al comunicarse con el servicio de clientes: " + e.getMessage());
        }

        Pedido pedido = mapper.toEntity(request);
        pedido = repository.save(pedido);
        return mapper.toDto(pedido);
    }

    public PedidoDTO obtenerPorId(Long id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
        return mapper.toDto(pedido);
    }

    public List<PedidoDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public PedidoDTO actualizarTotal(Long id, Double nuevoTotal) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));
        pedido.setTotal(nuevoTotal);
        return mapper.toDto(repository.save(pedido));
    }

    // ========== REPORTES ==========
    public List<PedidoDTO> findByCliente(Long clienteId) {
        return repository.findByClienteId(clienteId).stream()
                .map(mapper::toDto).collect(Collectors.toList());
    }

    public List<PedidoDTO> findByFechaRange(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null) inicio = LocalDateTime.MIN;
        if (fin == null) fin = LocalDateTime.now();
        return repository.findByFechaCreacionBetween(inicio, fin).stream()
                .map(mapper::toDto).collect(Collectors.toList());
    }
}
