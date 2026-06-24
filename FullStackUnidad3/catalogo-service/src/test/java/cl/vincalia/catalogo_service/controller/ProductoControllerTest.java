package cl.vincalia.catalogo_service;

import cl.vincalia.catalogo_service.controller.ProductoController;
import cl.vincalia.catalogo_service.dto.ProductoDTO;
import cl.vincalia.catalogo_service.dto.ProductoRequestDTO;
import cl.vincalia.catalogo_service.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
@DisplayName("Pruebas unitarias para ProductoController")
class ProductoControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private ProductoService service;
    @Autowired private ObjectMapper objectMapper;

    private ProductoDTO productoDTO;
    private ProductoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        productoDTO = new ProductoDTO();
        productoDTO.setId(1L);
        productoDTO.setNombre("Hamburguesa Clásica");
        productoDTO.setPrecio(5990.0);
        productoDTO.setCategoria("Comida");
        productoDTO.setDisponible(true);
        productoDTO.setStock(10);

        requestDTO = new ProductoRequestDTO();
        requestDTO.setNombre("Hamburguesa Clásica");
        requestDTO.setPrecio(5990.0);
        requestDTO.setCategoria("Comida");
        requestDTO.setStock(10);
    }

    @Test
    @DisplayName("POST /api/productos - debe crear producto y retornar 201")
    void testCrear_retorna201() throws Exception {
        when(service.crear(any(ProductoRequestDTO.class))).thenReturn(productoDTO);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Hamburguesa Clásica"));
    }

    @Test
    @DisplayName("GET /api/productos/{id} - debe retornar producto con 200")
    void testObtenerPorId_retorna200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(productoDTO);

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Hamburguesa Clásica"));
    }

    @Test
    @DisplayName("GET /api/productos - debe retornar lista con 200")
    void testListarTodos_retornaLista() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(productoDTO));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("DELETE /api/productos/{id} - debe retornar 204")
    void testEliminar_retorna204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }
}