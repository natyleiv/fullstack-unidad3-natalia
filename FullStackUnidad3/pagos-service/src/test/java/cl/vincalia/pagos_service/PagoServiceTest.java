package cl.vincalia.pagos_service;

import cl.vincalia.pagos_service.clients.PedidoFeignClient;
import cl.vincalia.pagos_service.dto.PagoDTO;
import cl.vincalia.pagos_service.dto.PagoRequestDTO;
import cl.vincalia.pagos_service.dto.PedidoDTO;
import cl.vincalia.pagos_service.exception.ResourceNotFoundException;
import cl.vincalia.pagos_service.mapper.PagoMapper;
import cl.vincalia.pagos_service.model.Pago;
import cl.vincalia.pagos_service.repository.PagoRepository;
import cl.vincalia.pagos_service.service.PagoService;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para PagoService")
class PagoServiceTest {

    @Mock private PagoRepository repository;
    @Mock private PagoMapper mapper;
    @Mock private PedidoFeignClient pedidoClient;
    @InjectMocks private PagoService service;

    private Pago pago;
    private PagoDTO pagoDTO;
    private PagoRequestDTO requestDTO;
    private PedidoDTO pedidoDTO;

    @BeforeEach
    void setUp() {
        pago = new Pago();
        pago.setId(1L);
        pago.setPedidoId(5L);
        pago.setMonto(4500.0);
        pago.setMetodoPago("TARJETA_CREDITO");
        pago.setEstado("COMPLETADO");
        pago.setFechaPago(LocalDateTime.now());
        pago.setReferencia("TX-001");

        pagoDTO = new PagoDTO();
        pagoDTO.setId(1L);
        pagoDTO.setPedidoId(5L);
        pagoDTO.setMonto(4500.0);
        pagoDTO.setMetodoPago("TARJETA_CREDITO");
        pagoDTO.setEstado("COMPLETADO");
        pagoDTO.setFechaPago(pago.getFechaPago());
        pagoDTO.setReferencia("TX-001");

        requestDTO = new PagoRequestDTO();
        requestDTO.setPedidoId(5L);
        requestDTO.setMonto(4500.0);
        requestDTO.setMetodoPago("TARJETA_CREDITO");
        requestDTO.setReferencia("TX-001");

        pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(5L);
        pedidoDTO.setTotal(4500.0);
    }

    @Test
    @DisplayName("Crear pago con pedido existente - debe retornar PagoDTO")
    void testCrearPago_pedidoExiste_retornaDTO() {
        when(pedidoClient.obtenerPedidoPorId(5L)).thenReturn(pedidoDTO);
        when(mapper.toEntity(requestDTO)).thenReturn(pago);
        when(repository.save(pago)).thenReturn(pago);
        when(mapper.toDto(pago)).thenReturn(pagoDTO);

        PagoDTO resultado = service.crearPago(requestDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(4500.0, resultado.getMonto());
        verify(repository, times(1)).save(pago);
    }

    @Test
    @DisplayName("Crear pago con pedido inexistente - debe lanzar ResourceNotFoundException")
    void testCrearPago_pedidoNoExiste_lanzaException() {
        FeignException.NotFound notFound = mock(FeignException.NotFound.class);
        when(pedidoClient.obtenerPedidoPorId(5L)).thenThrow(notFound);

        assertThrows(ResourceNotFoundException.class, () -> service.crearPago(requestDTO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Crear pago con error Feign - debe lanzar RuntimeException")
    void testCrearPago_errorFeign_lanzaRuntimeException() {
        FeignException feignEx = mock(FeignException.class);
        when(pedidoClient.obtenerPedidoPorId(5L)).thenThrow(feignEx);

        assertThrows(RuntimeException.class, () -> service.crearPago(requestDTO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Obtener pago por ID existente - debe retornar PagoDTO")
    void testObtenerPorId_existente_retornaDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(pago));
        when(mapper.toDto(pago)).thenReturn(pagoDTO);

        PagoDTO resultado = service.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Obtener pago por ID inexistente - debe lanzar ResourceNotFoundException")
    void testObtenerPorId_noExiste_lanzaException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    @DisplayName("Listar todos los pagos - debe retornar lista")
    void testListarTodos_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(pago));
        when(mapper.toDto(pago)).thenReturn(pagoDTO);

        List<PagoDTO> resultado = service.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Actualizar estado de pago existente - debe retornar PagoDTO actualizado")
    void testActualizarEstado_existente_retornaDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(pago));
        when(repository.save(pago)).thenReturn(pago);
        when(mapper.toDto(pago)).thenReturn(pagoDTO);

        PagoDTO resultado = service.actualizarEstado(1L, "REEMBOLSADO");

        assertEquals("REEMBOLSADO", pago.getEstado());
        verify(repository, times(1)).save(pago);
    }

    @Test
    @DisplayName("Actualizar estado de pago inexistente - debe lanzar ResourceNotFoundException")
    void testActualizarEstado_noExiste_lanzaException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.actualizarEstado(99L, "COMPLETADO"));
    }
}
