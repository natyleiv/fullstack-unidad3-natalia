package cl.vincalia.pedidos_service;

import cl.vincalia.pedidos_service.clients.ClienteFeignClient;
import cl.vincalia.pedidos_service.dto.ClienteDTO;
import cl.vincalia.pedidos_service.dto.PedidoDTO;
import cl.vincalia.pedidos_service.dto.PedidoRequestDTO;
import cl.vincalia.pedidos_service.exception.BusinessException;
import cl.vincalia.pedidos_service.exception.ResourceNotFoundException;
import cl.vincalia.pedidos_service.mapper.PedidoMapper;
import cl.vincalia.pedidos_service.model.Pedido;
import cl.vincalia.pedidos_service.repository.PedidoRepository;
import cl.vincalia.pedidos_service.service.PedidoService;
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
@DisplayName("Pruebas unitarias para PedidoService")
class PedidoServiceTest {

    @Mock
    private PedidoRepository repository;

    @Mock
    private PedidoMapper mapper;

    @Mock
    private ClienteFeignClient clienteClient;

    @InjectMocks
    private PedidoService service;

    private Pedido pedido;
    private PedidoDTO pedidoDTO;
    private PedidoRequestDTO requestDTO;
    private ClienteDTO clienteActivo;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setClienteId(10L);
        pedido.setTotal(5000.0);
        pedido.setFechaCreacion(LocalDateTime.now());
        pedido.setObservaciones("Sin cebolla");

        pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(1L);
        pedidoDTO.setClienteId(10L);
        pedidoDTO.setTotal(5000.0);
        pedidoDTO.setFechaCreacion(pedido.getFechaCreacion());
        pedidoDTO.setObservaciones("Sin cebolla");

        requestDTO = new PedidoRequestDTO();
        requestDTO.setClienteId(10L);
        requestDTO.setTotal(5000.0);
        requestDTO.setObservaciones("Sin cebolla");

        clienteActivo = new ClienteDTO();
        clienteActivo.setId(10L);
        clienteActivo.setActivo(true);
    }

    // ================= CREAR PEDIDO =================

    @Test
    @DisplayName("Crear pedido con cliente activo - debe retornar PedidoDTO")
    void testCrearPedido_clienteActivo_retornaDTO() {
        // arrange
        when(clienteClient.obtenerClientePorId(10L)).thenReturn(clienteActivo);
        when(mapper.toEntity(requestDTO)).thenReturn(pedido);
        when(repository.save(pedido)).thenReturn(pedido);
        when(mapper.toDto(pedido)).thenReturn(pedidoDTO);

        // act
        PedidoDTO resultado = service.crearPedido(requestDTO);

        // assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(10L, resultado.getClienteId());
        assertEquals(5000.0, resultado.getTotal());
        verify(repository, times(1)).save(pedido);
    }

    @Test
    @DisplayName("Crear pedido con cliente inactivo - debe lanzar BusinessException")
    void testCrearPedido_clienteInactivo_lanzaBusinessException() {
        // arrange
        clienteActivo.setActivo(false);
        when(clienteClient.obtenerClientePorId(10L)).thenReturn(clienteActivo);

        // act & assert
        assertThrows(BusinessException.class, () -> service.crearPedido(requestDTO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Crear pedido con cliente inexistente (404) - debe lanzar ResourceNotFoundException")
    void testCrearPedido_clienteNoExiste_lanzaNotFoundException() {
        // arrange
        FeignException.NotFound notFound = mock(FeignException.NotFound.class);
        when(clienteClient.obtenerClientePorId(10L)).thenThrow(notFound);

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> service.crearPedido(requestDTO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Crear pedido con error de comunicacion Feign - debe lanzar BusinessException")
    void testCrearPedido_errorFeign_lanzaBusinessException() {
        // arrange
        FeignException feignEx = mock(FeignException.class);
        when(clienteClient.obtenerClientePorId(10L)).thenThrow(feignEx);

        // act & assert
        assertThrows(BusinessException.class, () -> service.crearPedido(requestDTO));
        verify(repository, never()).save(any());
    }

    // ================= OBTENER POR ID =================

    @Test
    @DisplayName("Obtener pedido por id existente - debe retornar PedidoDTO")
    void testObtenerPorId_existente_retornaDTO() {
        // arrange
        when(repository.findById(1L)).thenReturn(Optional.of(pedido));
        when(mapper.toDto(pedido)).thenReturn(pedidoDTO);

        // act
        PedidoDTO resultado = service.obtenerPorId(1L);

        // assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Obtener pedido por id inexistente - debe lanzar ResourceNotFoundException")
    void testObtenerPorId_noExiste_lanzaNotFoundException() {
        // arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    // ================= LISTAR TODOS =================

    @Test
    @DisplayName("Listar todos los pedidos - debe retornar lista con un elemento")
    void testListarTodos_retornaLista() {
        // arrange
        when(repository.findAll()).thenReturn(List.of(pedido));
        when(mapper.toDto(pedido)).thenReturn(pedidoDTO);

        // act
        List<PedidoDTO> resultado = service.listarTodos();

        // assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    // ================= ACTUALIZAR TOTAL =================

    @Test
    @DisplayName("Actualizar total de pedido existente - debe retornar DTO con nuevo total")
    void testActualizarTotal_existente_actualizaTotal() {
        // arrange
        when(repository.findById(1L)).thenReturn(Optional.of(pedido));
        when(repository.save(pedido)).thenReturn(pedido);
        when(mapper.toDto(pedido)).thenReturn(pedidoDTO);

        // act
        PedidoDTO resultado = service.actualizarTotal(1L, 9999.0);

        // assert
        assertNotNull(resultado);
        verify(repository, times(1)).save(pedido);
        assertEquals(9999.0, pedido.getTotal());
    }

    @Test
    @DisplayName("Actualizar total de pedido inexistente - debe lanzar ResourceNotFoundException")
    void testActualizarTotal_noExiste_lanzaNotFoundException() {
        // arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> service.actualizarTotal(99L, 100.0));
    }
}
