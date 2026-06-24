package cl.vincalia.inventario_service.controller;

import cl.vincalia.inventario_service.dto.IngredienteDTO;
import cl.vincalia.inventario_service.dto.IngredienteRequestDTO;
import cl.vincalia.inventario_service.service.IngredienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IngredienteController.class)
@DisplayName("Pruebas unitarias para IngredienteController")
class IngredienteControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private IngredienteService service;
    @Autowired private ObjectMapper objectMapper;

    private IngredienteDTO ingredienteDTO;
    private IngredienteRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        ingredienteDTO = new IngredienteDTO();
        ingredienteDTO.setId(1L);
        ingredienteDTO.setNombre("Pan de completo");
        ingredienteDTO.setStock(100);
        ingredienteDTO.setPrecio(300.0);
        ingredienteDTO.setDisponible(true);
        ingredienteDTO.setUnidad("unidad");
        ingredienteDTO.setFechaCreacion(LocalDateTime.now());

        requestDTO = new IngredienteRequestDTO();
        requestDTO.setNombre("Pan de completo");
        requestDTO.setStock(100);
        requestDTO.setPrecio(300.0);
        requestDTO.setUnidad("unidad");
    }

    @Test
    @DisplayName("POST /api/ingredientes - debe crear ingrediente y retornar 201")
    void testCrear_retorna201() throws Exception {
        when(service.crear(any(IngredienteRequestDTO.class))).thenReturn(ingredienteDTO);

        mockMvc.perform(post("/api/ingredientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Pan de completo"));
    }

    @Test
    @DisplayName("GET /api/ingredientes/{id} - debe retornar ingrediente con 200")
    void testObtenerPorId_retorna200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(ingredienteDTO);

        mockMvc.perform(get("/api/ingredientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pan de completo"));
    }

    @Test
    @DisplayName("GET /api/ingredientes - debe retornar lista con 200")
    void testListarTodos_retornaLista() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(ingredienteDTO));

        mockMvc.perform(get("/api/ingredientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("DELETE /api/ingredientes/{id} - debe retornar 204")
    void testEliminar_retorna204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/ingredientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/ingredientes/reportes/disponibles - debe retornar lista")
    void testGetDisponibles_retornaLista() throws Exception {
        when(service.findDisponibles()).thenReturn(List.of(ingredienteDTO));

        mockMvc.perform(get("/api/ingredientes/reportes/disponibles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
