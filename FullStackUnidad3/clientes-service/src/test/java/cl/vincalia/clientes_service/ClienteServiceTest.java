package cl.vincalia.clientes_service;

import cl.vincalia.clientes_service.dto.ClienteDTO;
import cl.vincalia.clientes_service.exception.DuplicateResourceException;
import cl.vincalia.clientes_service.exception.ResourceNotFoundException;
import cl.vincalia.clientes_service.mapper.ClienteMapper;
import cl.vincalia.clientes_service.model.Cliente;
import cl.vincalia.clientes_service.repository.ClienteRepository;
import cl.vincalia.clientes_service.service.ClienteService;
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
@DisplayName("Pruebas unitarias para ClienteService")
class ClienteServiceTest {

    @Mock private ClienteRepository repository;
    @Mock private ClienteMapper mapper;
    @InjectMocks private ClienteService service;

    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setRut("12345678-9");
        cliente.setNombreCompleto("Juan Pérez");
        cliente.setEmail("juan@mail.com");
        cliente.setTelefono("912345678");
        cliente.setDireccion("Av. Principal 123");
        cliente.setFechaRegistro(LocalDateTime.now());
        cliente.setActivo(true);

        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setRut("12345678-9");
        clienteDTO.setNombreCompleto("Juan Pérez");
        clienteDTO.setEmail("juan@mail.com");
        clienteDTO.setTelefono("912345678");
        clienteDTO.setDireccion("Av. Principal 123");
        clienteDTO.setFechaRegistro(cliente.getFechaRegistro());
        clienteDTO.setActivo(true);
    }

    @Test
    @DisplayName("Crear cliente con datos únicos - debe retornar ClienteDTO")
    void testCrear_datosUnicos_retornaDTO() {
        when(repository.findByRut("12345678-9")).thenReturn(Optional.empty());
        when(repository.findByEmail("juan@mail.com")).thenReturn(Optional.empty());
        when(repository.findByTelefono("912345678")).thenReturn(Optional.empty());
        when(mapper.toEntity(clienteDTO)).thenReturn(cliente);
        when(repository.save(any())).thenReturn(cliente);
        when(mapper.toDto(cliente)).thenReturn(clienteDTO);

        ClienteDTO resultado = service.create(clienteDTO);

        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRut());
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Crear cliente con RUT duplicado - debe lanzar DuplicateResourceException")
    void testCrear_rutDuplicado_lanzaException() {
        when(repository.findByRut("12345678-9")).thenReturn(Optional.of(cliente));

        assertThrows(DuplicateResourceException.class, () -> service.create(clienteDTO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Obtener cliente por id existente - debe retornar ClienteDTO")
    void testFindById_existente_retornaDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));
        when(mapper.toDto(cliente)).thenReturn(clienteDTO);

        ClienteDTO resultado = service.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Obtener cliente por id inexistente - debe lanzar ResourceNotFoundException")
    void testFindById_noExiste_lanzaException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    @DisplayName("Listar todos los clientes - debe retornar lista")
    void testFindAll_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(cliente));
        when(mapper.toDto(cliente)).thenReturn(clienteDTO);

        List<ClienteDTO> resultado = service.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Eliminar cliente - debe hacer borrado lógico (activo=false)")
    void testDelete_existente_borradoLogico() {
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));
        when(repository.save(any())).thenReturn(cliente);

        service.delete(1L);

        assertFalse(cliente.getActivo());
        verify(repository, times(1)).save(cliente);
    }

    @Test
    @DisplayName("Eliminar cliente inexistente - debe lanzar ResourceNotFoundException")
    void testDelete_noExiste_lanzaException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));
    }
}