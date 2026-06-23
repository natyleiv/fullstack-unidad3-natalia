package cl.vincalia.delivery_service;

import cl.vincalia.delivery_service.controller.EntregaController;
import cl.vincalia.delivery_service.dto.EntregaDTO;
import cl.vincalia.delivery_service.dto.EntregaRequestDTO;
import cl.vincalia.delivery_service.service.EntregaService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EntregaController.class)
@DisplayName("Pruebas unitarias para EntregaController")
class EntregaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EntregaService service;

    @Autowired
    private ObjectMapper objectMapper;

    private EntregaDTO entregaDTO;
    private EntregaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        entregaDTO = new EntregaDTO();
        entregaDTO.setId(1L);
        entregaDTO.setPedidoId(5L);
        entregaDTO.setDireccion("Av. Providencia 123, Santiago");
        entregaDTO.setEstado("PENDIENTE");
        entregaDTO.setRepartidor("Juan Perez");
        entregaDTO.setFechaCreacion(LocalDateTime.now());

        requestDTO = new EntregaRequestDTO();
        requestDTO.setPedidoId(5L);
        requestDTO.setDireccion("Av. Providencia 123, Santiago");
        requestDTO.setEstado("PENDIENTE");
        requestDTO.setRepartidor("Juan Perez");
    }

    @Test
    @DisplayName("POST /api/entregas - debe crear entrega y retornar 201")
    void testCrear_retorna201() throws Exception {
        when(service.crearEntrega(any(EntregaRequestDTO.class))).thenReturn(entregaDTO);

        mockMvc.perform(post("/api/entregas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.pedidoId").value(5L))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    @DisplayName("GET /api/entregas/{id} - debe retornar entrega existente con 200")
    void testObtenerPorId_retorna200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(entregaDTO);

        mockMvc.perform(get("/api/entregas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.direccion").value("Av. Providencia 123, Santiago"));
    }

    @Test
    @DisplayName("GET /api/entregas - debe retornar lista con 200")
    void testListarTodos_retornaLista() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(entregaDTO));

        mockMvc.perform(get("/api/entregas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));
    }

    @Test
    @DisplayName("PUT /api/entregas/{id}/estado - debe actualizar estado y retornar 200")
    void testActualizarEstado_retorna200() throws Exception {
        entregaDTO.setEstado("EN_CAMINO");
        when(service.actualizarEstado(eq(1L), eq("EN_CAMINO"))).thenReturn(entregaDTO);

        mockMvc.perform(put("/api/entregas/1/estado")
                        .param("estado", "EN_CAMINO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_CAMINO"));
    }

    @Test
    @DisplayName("DELETE /api/entregas/{id} - debe eliminar y retornar 204")
    void testEliminar_retorna204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/entregas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/entregas/reportes/estado - debe retornar lista por estado")
    void testGetByEstado_retornaLista() throws Exception {
        when(service.findByEstado("PENDIENTE")).thenReturn(List.of(entregaDTO));

        mockMvc.perform(get("/api/entregas/reportes/estado")
                        .param("estado", "PENDIENTE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}