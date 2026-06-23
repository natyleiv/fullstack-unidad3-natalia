package cl.vincalia.pagos_service.service;

import cl.vincalia.pagos_service.clients.PedidoFeignClient;
import cl.vincalia.pagos_service.dto.PagoDTO;
import cl.vincalia.pagos_service.dto.PagoRequestDTO;
import cl.vincalia.pagos_service.dto.PedidoDTO;
import cl.vincalia.pagos_service.exception.DuplicateResourceException;
import cl.vincalia.pagos_service.exception.ResourceNotFoundException;
import cl.vincalia.pagos_service.mapper.PagoMapper;
import cl.vincalia.pagos_service.model.Pago;
import cl.vincalia.pagos_service.repository.PagoRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository repository;
    private final PagoMapper mapper;
    private final PedidoFeignClient pedidoClient;

    @Transactional
    public PagoDTO crearPago(PagoRequestDTO request) {
        // Validar que el pedido existe (vía Feign)
        try {
            PedidoDTO pedido = pedidoClient.obtenerPedidoPorId(request.getPedidoId());
            if (pedido == null) {
                throw new ResourceNotFoundException("No existe pedido con id: " + request.getPedidoId());
            }
            if (!pedido.getTotal().equals(request.getMonto())) {
                throw new DuplicateResourceException("El monto del pago (" + request.getMonto() + ") no coincide con el total del pedido (" + pedido.getTotal() + ")");
            }
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("No existe pedido con id: " + request.getPedidoId());
        } catch (FeignException e) {
            throw new RuntimeException("Error al comunicarse con el servicio de pedidos: " + e.getMessage());
        }

        Pago pago = mapper.toEntity(request);
        pago = repository.save(pago);
        return mapper.toDto(pago);
    }

    public PagoDTO obtenerPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id: " + id));
    }

    public List<PagoDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public PagoDTO actualizarEstado(Long id, String nuevoEstado) {
        Pago pago = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));
        pago.setEstado(nuevoEstado);
        return mapper.toDto(repository.save(pago));
    }

    // ========== REPORTES ==========
    public List<PagoDTO> findByPedido(Long pedidoId) {
        return repository.findByPedidoId(pedidoId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<PagoDTO> findByEstado(String estado) {
        return repository.findByEstado(estado).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<PagoDTO> findByMetodoPago(String metodoPago) {
        return repository.findByMetodoPago(metodoPago).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<PagoDTO> findByFechaRange(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null) inicio = LocalDateTime.MIN;
        if (fin == null) fin = LocalDateTime.now();
        return repository.findByFechaPagoBetween(inicio, fin).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<PagoDTO> findByMontoRange(Double min, Double max) {
        if (min == null) min = 0.0;
        if (max == null) max = Double.MAX_VALUE;
        return repository.findByMontoBetween(min, max).stream().map(mapper::toDto).collect(Collectors.toList());
    }
}