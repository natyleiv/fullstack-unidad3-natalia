package cl.vincalia.cupones_service;

import cl.vincalia.cupones_service.dto.CuponDTO;
import cl.vincalia.cupones_service.dto.CuponRequestDTO;
import cl.vincalia.cupones_service.exception.ResourceNotFoundException;
import cl.vincalia.cupones_service.mapper.CuponMapper;
import cl.vincalia.cupones_service.model.Cupon;
import cl.vincalia.cupones_service.repository.CuponRepository;
import cl.vincalia.cupones_service.service.CuponService;
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
@DisplayName("Pruebas unitarias para CuponService")
class CuponServiceTest {

    @Mock private CuponRepository repository;
    @Mock private CuponMapper mapper;
    @InjectMocks private CuponService service;

    private Cupon cupon;
    private CuponDTO cuponDTO;
    private CuponRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        cupon = new Cupon();
        cupon.setId(1L);
        cupon.setCodigo("DESC10");
        cupon.setDescuento(10.0);
        cupon.setFechaVencimiento(LocalDateTime.now().plusDays(30));
        cupon.setUsado(false);
        cupon.setActivo(true);
        cupon.setFechaCreacion(LocalDateTime.now());

        cuponDTO = new CuponDTO();
        cuponDTO.setId(1L);
        cuponDTO.setCodigo("DESC10");
        cuponDTO.setDescuento(10.0);
        cuponDTO.setFechaVencimiento(cupon.getFechaVencimiento());
        cuponDTO.setUsado(false);
        cuponDTO.setActivo(true);
        cuponDTO.setFechaCreacion(cupon.getFechaCreacion());

        requestDTO = new CuponRequestDTO();
        requestDTO.setCodigo("DESC10");
        requestDTO.setDescuento(10.0);
        requestDTO.setFechaVencimiento(LocalDateTime.now().plusDays(30));
    }

    @Test
    @DisplayName("Crear cupón con código único - debe retornar CuponDTO")
    void testCrear_codigoUnico_retornaDTO() {
        when(repository.findByCodigo("DESC10")).thenReturn(Optional.empty());
        when(mapper.toEntity(requestDTO)).thenReturn(cupon);
        when(repository.save(cupon)).thenReturn(cupon);
        when(mapper.toDto(cupon)).thenReturn(cuponDTO);

        CuponDTO resultado = service.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("DESC10", resultado.getCodigo());
        assertEquals(10.0, resultado.getDescuento());
        verify(repository, times(1)).save(cupon);
    }

    @Test
    @DisplayName("Obtener cupón por ID existente - debe retornar CuponDTO")
    void testObtenerPorId_existente_retornaDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(cupon));
        when(mapper.toDto(cupon)).thenReturn(cuponDTO);

        CuponDTO resultado = service.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Obtener cupón por ID inexistente - debe lanzar ResourceNotFoundException")
    void testObtenerPorId_noExiste_lanzaException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    @DisplayName("Listar todos los cupones - debe retornar lista")
    void testListarTodos_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(cupon));
        when(mapper.toDto(cupon)).thenReturn(cuponDTO);

        List<CuponDTO> resultado = service.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Eliminar cupón existente - debe llamar deleteById")
    void testEliminar_existente_eliminaCorrectamente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar cupón inexistente - debe lanzar ResourceNotFoundException")
    void testEliminar_noExiste_lanzaException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.eliminar(99L));
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Obtener cupón por código existente - debe retornar CuponDTO")
    void testObtenerPorCodigo_existente_retornaDTO() {
        when(repository.findByCodigo("DESC10")).thenReturn(Optional.of(cupon));
        when(mapper.toDto(cupon)).thenReturn(cuponDTO);

        CuponDTO resultado = service.obtenerPorCodigo("DESC10");

        assertNotNull(resultado);
        assertEquals("DESC10", resultado.getCodigo());
    }

    @Test
    @DisplayName("Reportes: cupones activos - debe retornar lista")
    void testFindActivos_retornaLista() {
        when(repository.findByActivoTrue()).thenReturn(List.of(cupon));
        when(mapper.toDto(cupon)).thenReturn(cuponDTO);

        List<CuponDTO> resultado = service.findActivos();

        assertEquals(1, resultado.size());
    }
}
