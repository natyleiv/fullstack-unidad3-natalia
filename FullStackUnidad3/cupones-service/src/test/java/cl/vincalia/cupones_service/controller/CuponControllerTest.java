package cl.vincalia.cupones_service.controller;

import cl.vincalia.cupones_service.dto.CuponDTO;
import cl.vincalia.cupones_service.dto.CuponRequestDTO;
import cl.vincalia.cupones_service.service.CuponService;
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

@WebMvcTest(CuponController.class)
@DisplayName("Pruebas unitarias para CuponController")
class CuponControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private CuponService service;
    @Autowired private ObjectMapper objectMapper;

    private CuponDTO cuponDTO;
    private CuponRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        cuponDTO = new CuponDTO();
        cuponDTO.setId(1L);
        cuponDTO.setCodigo("DESC10");
        cuponDTO.setDescuento(10.0);
        cuponDTO.setFechaVencimiento(LocalDateTime.now().plusDays(30));
        cuponDTO.setUsado(false);
        cuponDTO.setActivo(true);
        cuponDTO.setFechaCreacion(LocalDateTime.now());

        requestDTO = new CuponRequestDTO();
        requestDTO.setCodigo("DESC10");
        requestDTO.setDescuento(10.0);
        requestDTO.setFechaVencimiento(LocalDateTime.now().plusDays(30));
    }

    @Test
    @DisplayName("POST /api/cupones - debe crear cupón y retornar 201")
    void testCrear_retorna201() throws Exception {
        when(service.crear(any(CuponRequestDTO.class))).thenReturn(cuponDTO);

        mockMvc.perform(post("/api/cupones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.codigo").value("DESC10"));
    }

    @Test
    @DisplayName("GET /api/cupones/{id} - debe retornar cupón con 200")
    void testObtenerPorId_retorna200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(cuponDTO);

        mockMvc.perform(get("/api/cupones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("DESC10"));
    }

    @Test
    @DisplayName("GET /api/cupones - debe retornar lista con 200")
    void testListarTodos_retornaLista() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(cuponDTO));

        mockMvc.perform(get("/api/cupones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("DELETE /api/cupones/{id} - debe retornar 204")
    void testEliminar_retorna204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/cupones/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/cupones/reportes/activos - debe retornar lista")
    void testGetActivos_retornaLista() throws Exception {
        when(service.findActivos()).thenReturn(List.of(cuponDTO));

        mockMvc.perform(get("/api/cupones/reportes/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
