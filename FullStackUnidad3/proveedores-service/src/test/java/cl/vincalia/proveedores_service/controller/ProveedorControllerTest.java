package cl.vincalia.proveedores_service.controller;

import cl.vincalia.proveedores_service.dto.ProveedorDTO;
import cl.vincalia.proveedores_service.dto.ProveedorRequestDTO;
import cl.vincalia.proveedores_service.service.ProveedorService;
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

@WebMvcTest(ProveedorController.class)
@DisplayName("Pruebas unitarias para ProveedorController")
class ProveedorControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private ProveedorService service;
    @Autowired private ObjectMapper objectMapper;

    private ProveedorDTO proveedorDTO;
    private ProveedorRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        proveedorDTO = new ProveedorDTO();
        proveedorDTO.setId(1L);
        proveedorDTO.setRut("12345678-9");
        proveedorDTO.setRazonSocial("Panadería El Trigal");
        proveedorDTO.setRubro("Panadería");
        proveedorDTO.setEmailContacto("contacto@trigal.cl");
        proveedorDTO.setTelefono("912345678");
        proveedorDTO.setDireccion("Av. Panamericana 123");
        proveedorDTO.setActivo(true);
        proveedorDTO.setFechaRegistro(LocalDateTime.now());

        requestDTO = new ProveedorRequestDTO();
        requestDTO.setRut("12345678-9");
        requestDTO.setRazonSocial("Panadería El Trigal");
        requestDTO.setRubro("Panadería");
        requestDTO.setEmailContacto("contacto@trigal.cl");
        requestDTO.setTelefono("912345678");
        requestDTO.setDireccion("Av. Panamericana 123");
    }

    @Test
    @DisplayName("POST /api/proveedores - debe crear proveedor y retornar 201")
    void testCrear_retorna201() throws Exception {
        when(service.crear(any(ProveedorRequestDTO.class))).thenReturn(proveedorDTO);

        mockMvc.perform(post("/api/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    @DisplayName("GET /api/proveedores/{id} - debe retornar proveedor con 200")
    void testObtenerPorId_retorna200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(proveedorDTO);

        mockMvc.perform(get("/api/proveedores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.razonSocial").value("Panadería El Trigal"));
    }

    @Test
    @DisplayName("GET /api/proveedores - debe retornar lista con 200")
    void testListarTodos_retornaLista() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(proveedorDTO));

        mockMvc.perform(get("/api/proveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("DELETE /api/proveedores/{id} - debe retornar 204")
    void testEliminar_retorna204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/proveedores/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/proveedores/reportes/activos - debe retornar lista")
    void testGetActivos_retornaLista() throws Exception {
        when(service.findActivos()).thenReturn(List.of(proveedorDTO));

        mockMvc.perform(get("/api/proveedores/reportes/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
