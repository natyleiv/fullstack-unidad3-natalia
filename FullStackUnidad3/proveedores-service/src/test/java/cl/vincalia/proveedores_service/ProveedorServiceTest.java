package cl.vincalia.proveedores_service;

import cl.vincalia.proveedores_service.dto.ProveedorDTO;
import cl.vincalia.proveedores_service.dto.ProveedorRequestDTO;
import cl.vincalia.proveedores_service.exception.DuplicateResourceException;
import cl.vincalia.proveedores_service.exception.ResourceNotFoundException;
import cl.vincalia.proveedores_service.mapper.ProveedorMapper;
import cl.vincalia.proveedores_service.model.Proveedor;
import cl.vincalia.proveedores_service.repository.ProveedorRepository;
import cl.vincalia.proveedores_service.service.ProveedorService;
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
@DisplayName("Pruebas unitarias para ProveedorService")
class ProveedorServiceTest {

    @Mock private ProveedorRepository repository;
    @Mock private ProveedorMapper mapper;
    @InjectMocks private ProveedorService service;

    private Proveedor proveedor;
    private ProveedorDTO proveedorDTO;
    private ProveedorRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        proveedor = new Proveedor();
        proveedor.setId(1L);
        proveedor.setRut("12345678-9");
        proveedor.setRazonSocial("Panadería El Trigal");
        proveedor.setRubro("Panadería");
        proveedor.setEmailContacto("contacto@trigal.cl");
        proveedor.setTelefono("912345678");
        proveedor.setDireccion("Av. Panamericana 123");
        proveedor.setActivo(true);
        proveedor.setFechaRegistro(LocalDateTime.now());

        proveedorDTO = new ProveedorDTO();
        proveedorDTO.setId(1L);
        proveedorDTO.setRut("12345678-9");
        proveedorDTO.setRazonSocial("Panadería El Trigal");
        proveedorDTO.setRubro("Panadería");
        proveedorDTO.setEmailContacto("contacto@trigal.cl");
        proveedorDTO.setTelefono("912345678");
        proveedorDTO.setDireccion("Av. Panamericana 123");
        proveedorDTO.setActivo(true);
        proveedorDTO.setFechaRegistro(proveedor.getFechaRegistro());

        requestDTO = new ProveedorRequestDTO();
        requestDTO.setRut("12345678-9");
        requestDTO.setRazonSocial("Panadería El Trigal");
        requestDTO.setRubro("Panadería");
        requestDTO.setEmailContacto("contacto@trigal.cl");
        requestDTO.setTelefono("912345678");
        requestDTO.setDireccion("Av. Panamericana 123");
    }

    @Test
    @DisplayName("Crear proveedor con datos únicos - debe retornar ProveedorDTO")
    void testCrear_datosUnicos_retornaDTO() {
        when(repository.findByRut("12345678-9")).thenReturn(Optional.empty());
        when(repository.findByEmailContacto("contacto@trigal.cl")).thenReturn(Optional.empty());
        when(mapper.toEntity(requestDTO)).thenReturn(proveedor);
        when(repository.save(proveedor)).thenReturn(proveedor);
        when(mapper.toDto(proveedor)).thenReturn(proveedorDTO);

        ProveedorDTO resultado = service.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRut());
        assertEquals("Panadería El Trigal", resultado.getRazonSocial());
        verify(repository, times(1)).save(proveedor);
    }

    @Test
    @DisplayName("Crear proveedor con RUT duplicado - debe lanzar DuplicateResourceException")
    void testCrear_rutDuplicado_lanzaException() {
        when(repository.findByRut("12345678-9")).thenReturn(Optional.of(proveedor));

        assertThrows(DuplicateResourceException.class, () -> service.crear(requestDTO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Obtener proveedor por ID existente - debe retornar ProveedorDTO")
    void testObtenerPorId_existente_retornaDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(mapper.toDto(proveedor)).thenReturn(proveedorDTO);

        ProveedorDTO resultado = service.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Obtener proveedor por ID inexistente - debe lanzar ResourceNotFoundException")
    void testObtenerPorId_noExiste_lanzaException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    @DisplayName("Listar todos los proveedores - debe retornar lista")
    void testListarTodos_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(proveedor));
        when(mapper.toDto(proveedor)).thenReturn(proveedorDTO);

        List<ProveedorDTO> resultado = service.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Eliminar proveedor existente - debe llamar deleteById")
    void testEliminar_existente_eliminaCorrectamente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar proveedor inexistente - debe lanzar ResourceNotFoundException")
    void testEliminar_noExiste_lanzaException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.eliminar(99L));
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Reportes: proveedores activos - debe retornar lista")
    void testFindActivos_retornaLista() {
        when(repository.findByActivoTrue()).thenReturn(List.of(proveedor));
        when(mapper.toDto(proveedor)).thenReturn(proveedorDTO);

        List<ProveedorDTO> resultado = service.findActivos();

        assertEquals(1, resultado.size());
    }
}
