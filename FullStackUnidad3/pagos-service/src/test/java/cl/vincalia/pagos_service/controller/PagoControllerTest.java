package cl.vincalia.pagos_service.controller;

import cl.vincalia.pagos_service.dto.PagoDTO;
import cl.vincalia.pagos_service.dto.PagoRequestDTO;
import cl.vincalia.pagos_service.service.PagoService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagoController.class)
@DisplayName("Pruebas unitarias para PagoController")
class PagoControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private PagoService service;
    @Autowired private ObjectMapper objectMapper;

    private PagoDTO pagoDTO;
    private PagoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        pagoDTO = new PagoDTO();
        pagoDTO.setId(1L);
        pagoDTO.setPedidoId(5L);
        pagoDTO.setMonto(4500.0);
        pagoDTO.setMetodoPago("TARJETA_CREDITO");
        pagoDTO.setEstado("COMPLETADO");
        pagoDTO.setFechaPago(LocalDateTime.now());
        pagoDTO.setReferencia("TX-001");

        requestDTO = new PagoRequestDTO();
        requestDTO.setPedidoId(5L);
        requestDTO.setMonto(4500.0);
        requestDTO.setMetodoPago("TARJETA_CREDITO");
        requestDTO.setReferencia("TX-001");
    }

    @Test
    @DisplayName("POST /api/pagos - debe crear pago y retornar 201")
    void testCrear_retorna201() throws Exception {
        when(service.crearPago(any(PagoRequestDTO.class))).thenReturn(pagoDTO);

        mockMvc.perform(post("/api/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.monto").value(4500.0));
    }

    @Test
    @DisplayName("GET /api/pagos/{id} - debe retornar pago con 200")
    void testObtenerPorId_retorna200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(pagoDTO);

        mockMvc.perform(get("/api/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metodoPago").value("TARJETA_CREDITO"));
    }

    @Test
    @DisplayName("GET /api/pagos - debe retornar lista con 200")
    void testListarTodos_retornaLista() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(pagoDTO));

        mockMvc.perform(get("/api/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("PUT /api/pagos/{id}/estado - debe actualizar estado y retornar 200")
    void testActualizarEstado_retorna200() throws Exception {
        pagoDTO.setEstado("REEMBOLSADO");
        when(service.actualizarEstado(eq(1L), eq("REEMBOLSADO"))).thenReturn(pagoDTO);

        mockMvc.perform(put("/api/pagos/1/estado")
                        .param("estado", "REEMBOLSADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("REEMBOLSADO"));
    }

    @Test
    @DisplayName("GET /api/pagos/reportes/estado - debe retornar lista por estado")
    void testGetByEstado_retornaLista() throws Exception {
        when(service.findByEstado("COMPLETADO")).thenReturn(List.of(pagoDTO));

        mockMvc.perform(get("/api/pagos/reportes/estado")
                        .param("estado", "COMPLETADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
