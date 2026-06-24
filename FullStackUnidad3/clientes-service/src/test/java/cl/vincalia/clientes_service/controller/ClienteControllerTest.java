package cl.vincalia.clientes_service;

import cl.vincalia.clientes_service.controller.ClienteController;
import cl.vincalia.clientes_service.dto.ClienteDTO;
import cl.vincalia.clientes_service.service.ClienteService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@DisplayName("Pruebas unitarias para ClienteController")
class ClienteControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private ClienteService service;
    @Autowired private ObjectMapper objectMapper;

    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setRut("12345678-9");
        clienteDTO.setNombreCompleto("Juan Pérez");
        clienteDTO.setEmail("juan@mail.com");
        clienteDTO.setTelefono("912345678");
        clienteDTO.setDireccion("Av. Principal 123");
        clienteDTO.setFechaRegistro(LocalDateTime.now());
        clienteDTO.setActivo(true);
    }

    @Test
    @DisplayName("POST /api/clientes - debe crear cliente y retornar 201")
    void testCreate_retorna201() throws Exception {
        when(service.create(any(ClienteDTO.class))).thenReturn(clienteDTO);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    @DisplayName("GET /api/clientes/{id} - debe retornar cliente con 200")
    void testGetById_retorna200() throws Exception {
        when(service.findById(1L)).thenReturn(clienteDTO);

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Pérez"));
    }

    @Test
    @DisplayName("GET /api/clientes - debe retornar lista con 200")
    void testGetAll_retornaLista() throws Exception {
        when(service.findAll()).thenReturn(List.of(clienteDTO));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("DELETE /api/clientes/{id} - debe retornar 204")
    void testDelete_retorna204() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());
    }
}