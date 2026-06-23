package cl.vincalia.delivery_service.service;


import cl.vincalia.delivery_service.clients.PedidoFeignClient;
import feign.FeignException;
import cl.vincalia.delivery_service.dto.EntregaDTO;
import cl.vincalia.delivery_service.dto.EntregaRequestDTO;
import cl.vincalia.delivery_service.dto.PedidoDTO;
import cl.vincalia.delivery_service.exception.ResourceNotFoundException;
import cl.vincalia.delivery_service.mapper.EntregaMapper;
import cl.vincalia.delivery_service.model.Entrega;
import cl.vincalia.delivery_service.repository.EntregaRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntregaService {

    private final EntregaRepository repository;
    private final EntregaMapper mapper;
    private final PedidoFeignClient pedidoClient;

    @Transactional
    public EntregaDTO crearEntrega(EntregaRequestDTO request) {
        // Validar pedido existe (vía Feign)
        try {
            PedidoDTO pedido = pedidoClient.obtenerPedidoPorId(request.getPedidoId());
            if (pedido == null) {
                throw new ResourceNotFoundException("No existe pedido con id: " + request.getPedidoId());
            }
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("No existe pedido con id: " + request.getPedidoId());
        } catch (FeignException e) {
            throw new RuntimeException("Error al comunicarse con el servicio de pedidos: " + e.getMessage());
        }

        Entrega entrega = mapper.toEntity(request);
        entrega = repository.save(entrega);
        return mapper.toDto(entrega);
    }

    public EntregaDTO obtenerPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega no encontrada con id: " + id));
    }

    public List<EntregaDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public EntregaDTO actualizarEstado(Long id, String nuevoEstado) {
        Entrega entrega = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega no encontrada"));
        entrega.setEstado(nuevoEstado);
        if ("ENTREGADO".equals(nuevoEstado)) {
            entrega.setFechaEntregaReal(LocalDateTime.now());
        }
        return mapper.toDto(repository.save(entrega));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Entrega no encontrada");
        }
        repository.deleteById(id);
    }

    // ========== REPORTES ==========
    public List<EntregaDTO> findByPedido(Long pedidoId) {
        return repository.findByPedidoId(pedidoId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<EntregaDTO> findByEstado(String estado) {
        return repository.findByEstado(estado).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<EntregaDTO> findByRepartidor(String repartidor) {
        return repository.findByRepartidor(repartidor).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<EntregaDTO> findByFechaCreacionRange(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null) inicio = LocalDateTime.MIN;
        if (fin == null) fin = LocalDateTime.now();
        return repository.findByFechaCreacionBetween(inicio, fin).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<EntregaDTO> findByFechaEstimadaRange(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null) inicio = LocalDateTime.MIN;
        if (fin == null) fin = LocalDateTime.now();
        return repository.findByFechaEstimadaBetween(inicio, fin).stream().map(mapper::toDto).collect(Collectors.toList());
    }
}