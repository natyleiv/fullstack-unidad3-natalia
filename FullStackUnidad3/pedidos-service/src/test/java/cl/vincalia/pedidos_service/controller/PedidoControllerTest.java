package cl.vincalia.pedidos_service.controller;

import cl.vincalia.pedidos_service.dto.PedidoDTO;
import cl.vincalia.pedidos_service.dto.PedidoRequestDTO;
import cl.vincalia.pedidos_service.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
@DisplayName("Pruebas unitarias para PedidoController")
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedidoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private PedidoDTO pedidoDTO;
    private PedidoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(1L);
        pedidoDTO.setClienteId(10L);
        pedidoDTO.setTotal(5000.0);
        pedidoDTO.setFechaCreacion(LocalDateTime.now());
        pedidoDTO.setObservaciones("Sin cebolla");

        requestDTO = new PedidoRequestDTO();
        requestDTO.setClienteId(10L);
        requestDTO.setTotal(5000.0);
        requestDTO.setObservaciones("Sin cebolla");
    }

    @Test
    @DisplayName("POST /api/pedidos - debe crear pedido y retornar 201")
    void testCrear_retorna201() throws Exception {
        when(service.crearPedido(any(PedidoRequestDTO.class))).thenReturn(pedidoDTO);

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.clienteId").value(10L))
                .andExpect(jsonPath("$.total").value(5000.0));
    }

    @Test
    @DisplayName("GET /api/pedidos/{id} - debe retornar pedido existente con 200")
    void testObtenerPorId_retorna200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(pedidoDTO);

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.observaciones").value("Sin cebolla"));
    }

    @Test
    @DisplayName("GET /api/pedidos - debe retornar lista con 200")
    void testListarTodos_retornaLista() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(pedidoDTO));

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("PUT /api/pedidos/{id}/total - debe actualizar total y retornar 200")
    void testActualizarTotal_retorna200() throws Exception {
        pedidoDTO.setTotal(9999.0);
        when(service.actualizarTotal(eq(1L), eq(9999.0))).thenReturn(pedidoDTO);

        mockMvc.perform(put("/api/pedidos/1/total")
                        .param("total", "9999.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(9999.0));
    }

    @Test
    @DisplayName("GET /api/pedidos/reportes/cliente/{clienteId} - debe retornar lista")
    void testGetByCliente_retornaLista() throws Exception {
        when(service.findByCliente(10L)).thenReturn(List.of(pedidoDTO));

        mockMvc.perform(get("/api/pedidos/reportes/cliente/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}