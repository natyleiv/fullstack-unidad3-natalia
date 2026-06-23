package cl.vincalia.delivery_service;

import cl.vincalia.delivery_service.clients.PedidoFeignClient;
import cl.vincalia.delivery_service.dto.EntregaDTO;
import cl.vincalia.delivery_service.dto.EntregaRequestDTO;
import cl.vincalia.delivery_service.dto.PedidoDTO;
import cl.vincalia.delivery_service.exception.ResourceNotFoundException;
import cl.vincalia.delivery_service.mapper.EntregaMapper;
import cl.vincalia.delivery_service.model.Entrega;
import cl.vincalia.delivery_service.repository.EntregaRepository;
import cl.vincalia.delivery_service.service.EntregaService;
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
@DisplayName("Pruebas unitarias para EntregaService")
class EntregaServiceTest {

    @Mock
    private EntregaRepository repository;

    @Mock
    private EntregaMapper mapper;

    @Mock
    private PedidoFeignClient pedidoClient;

    @InjectMocks
    private EntregaService service;

    private Entrega entrega;
    private EntregaDTO entregaDTO;
    private EntregaRequestDTO requestDTO;
    private PedidoDTO pedidoDTO;

    @BeforeEach
    void setUp() {
        entrega = new Entrega();
        entrega.setId(1L);
        entrega.setPedidoId(5L);
        entrega.setDireccion("Av. Providencia 123, Santiago");
        entrega.setEstado("PENDIENTE");
        entrega.setRepartidor("Juan Perez");
        entrega.setFechaCreacion(LocalDateTime.now());

        entregaDTO = new EntregaDTO();
        entregaDTO.setId(1L);
        entregaDTO.setPedidoId(5L);
        entregaDTO.setDireccion("Av. Providencia 123, Santiago");
        entregaDTO.setEstado("PENDIENTE");
        entregaDTO.setRepartidor("Juan Perez");
        entregaDTO.setFechaCreacion(entrega.getFechaCreacion());

        requestDTO = new EntregaRequestDTO();
        requestDTO.setPedidoId(5L);
        requestDTO.setDireccion("Av. Providencia 123, Santiago");
        requestDTO.setEstado("PENDIENTE");
        requestDTO.setRepartidor("Juan Perez");

        pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(5L);
        pedidoDTO.setClienteId(10L);
        pedidoDTO.setTotal(5000.0);
    }

    // ================= CREAR ENTREGA =================

    @Test
    @DisplayName("Crear entrega con pedido existente - debe retornar EntregaDTO")
    void testCrearEntrega_pedidoExiste_retornaDTO() {
        // arrange
        when(pedidoClient.obtenerPedidoPorId(5L)).thenReturn(pedidoDTO);
        when(mapper.toEntity(requestDTO)).thenReturn(entrega);
        when(repository.save(entrega)).thenReturn(entrega);
        when(mapper.toDto(entrega)).thenReturn(entregaDTO);

        // act
        EntregaDTO resultado = service.crearEntrega(requestDTO);

        // assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(5L, resultado.getPedidoId());
        assertEquals("PENDIENTE", resultado.getEstado());
        verify(repository, times(1)).save(entrega);
    }

    @Test
    @DisplayName("Crear entrega con pedido inexistente (404) - debe lanzar ResourceNotFoundException")
    void testCrearEntrega_pedidoNoExiste_lanzaNotFoundException() {
        // arrange
        FeignException.NotFound notFound = mock(FeignException.NotFound.class);
        when(pedidoClient.obtenerPedidoPorId(5L)).thenThrow(notFound);

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> service.crearEntrega(requestDTO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Crear entrega con error de comunicacion Feign - debe lanzar RuntimeException")
    void testCrearEntrega_errorFeign_lanzaRuntimeException() {
        // arrange
        FeignException feignEx = mock(FeignException.class);
        when(pedidoClient.obtenerPedidoPorId(5L)).thenThrow(feignEx);

        // act & assert
        assertThrows(RuntimeException.class, () -> service.crearEntrega(requestDTO));
        verify(repository, never()).save(any());
    }

    // ================= OBTENER POR ID =================

    @Test
    @DisplayName("Obtener entrega por id existente - debe retornar EntregaDTO")
    void testObtenerPorId_existente_retornaDTO() {
        // arrange
        when(repository.findById(1L)).thenReturn(Optional.of(entrega));
        when(mapper.toDto(entrega)).thenReturn(entregaDTO);

        // act
        EntregaDTO resultado = service.obtenerPorId(1L);

        // assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Obtener entrega por id inexistente - debe lanzar ResourceNotFoundException")
    void testObtenerPorId_noExiste_lanzaNotFoundException() {
        // arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    // ================= LISTAR TODOS =================

    @Test
    @DisplayName("Listar todas las entregas - debe retornar lista con un elemento")
    void testListarTodos_retornaLista() {
        // arrange
        when(repository.findAll()).thenReturn(List.of(entrega));
        when(mapper.toDto(entrega)).thenReturn(entregaDTO);

        // act
        List<EntregaDTO> resultado = service.listarTodos();

        // assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    // ================= ACTUALIZAR ESTADO =================

    @Test
    @DisplayName("Actualizar estado a ENTREGADO - debe registrar fecha de entrega real")
    void testActualizarEstado_entregado_registraFechaEntrega() {
        // arrange
        when(repository.findById(1L)).thenReturn(Optional.of(entrega));
        when(repository.save(entrega)).thenReturn(entrega);
        when(mapper.toDto(entrega)).thenReturn(entregaDTO);

        // act
        service.actualizarEstado(1L, "ENTREGADO");

        // assert
        assertEquals("ENTREGADO", entrega.getEstado());
        assertNotNull(entrega.getFechaEntregaReal());
        verify(repository, times(1)).save(entrega);
    }

    @Test
    @DisplayName("Actualizar estado a EN_CAMINO - no debe registrar fecha de entrega real")
    void testActualizarEstado_enCamino_noRegistraFechaEntrega() {
        // arrange
        when(repository.findById(1L)).thenReturn(Optional.of(entrega));
        when(repository.save(entrega)).thenReturn(entrega);
        when(mapper.toDto(entrega)).thenReturn(entregaDTO);

        // act
        service.actualizarEstado(1L, "EN_CAMINO");

        // assert
        assertEquals("EN_CAMINO", entrega.getEstado());
        assertNull(entrega.getFechaEntregaReal());
    }

    @Test
    @DisplayName("Actualizar estado de entrega inexistente - debe lanzar ResourceNotFoundException")
    void testActualizarEstado_noExiste_lanzaNotFoundException() {
        // arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> service.actualizarEstado(99L, "ENTREGADO"));
    }

    // ================= ELIMINAR =================

    @Test
    @DisplayName("Eliminar entrega existente - debe llamar deleteById")
    void testEliminar_existente_eliminaCorrectamente() {
        // arrange
        when(repository.existsById(1L)).thenReturn(true);

        // act
        service.eliminar(1L);

        // assert
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar entrega inexistente - debe lanzar ResourceNotFoundException")
    void testEliminar_noExiste_lanzaNotFoundException() {
        // arrange
        when(repository.existsById(99L)).thenReturn(false);

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> service.eliminar(99L));
        verify(repository, never()).deleteById(any());
    }
}
