package cl.vincalia.inventario_service;

import cl.vincalia.inventario_service.clients.ProductoFeignClient;
import cl.vincalia.inventario_service.dto.IngredienteDTO;
import cl.vincalia.inventario_service.dto.IngredienteRequestDTO;
import cl.vincalia.inventario_service.exception.BusinessException;
import cl.vincalia.inventario_service.exception.ResourceNotFoundException;
import cl.vincalia.inventario_service.mapper.IngredienteMapper;
import cl.vincalia.inventario_service.model.Ingrediente;
import cl.vincalia.inventario_service.repository.IngredienteRepository;
import cl.vincalia.inventario_service.service.IngredienteService;
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
@DisplayName("Pruebas unitarias para IngredienteService")
class IngredienteServiceTest {

    @Mock private IngredienteRepository repository;
    @Mock private IngredienteMapper mapper;
    @Mock private ProductoFeignClient productoClient;
    @InjectMocks private IngredienteService service;

    private Ingrediente ingrediente;
    private IngredienteDTO ingredienteDTO;
    private IngredienteRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        ingrediente = new Ingrediente();
        ingrediente.setId(1L);
        ingrediente.setNombre("Pan de completo");
        ingrediente.setStock(100);
        ingrediente.setPrecio(300.0);
        ingrediente.setDisponible(true);
        ingrediente.setUnidad("unidad");
        ingrediente.setFechaCreacion(LocalDateTime.now());

        ingredienteDTO = new IngredienteDTO();
        ingredienteDTO.setId(1L);
        ingredienteDTO.setNombre("Pan de completo");
        ingredienteDTO.setStock(100);
        ingredienteDTO.setPrecio(300.0);
        ingredienteDTO.setDisponible(true);
        ingredienteDTO.setUnidad("unidad");
        ingredienteDTO.setFechaCreacion(ingrediente.getFechaCreacion());

        requestDTO = new IngredienteRequestDTO();
        requestDTO.setNombre("Pan de completo");
        requestDTO.setStock(100);
        requestDTO.setPrecio(300.0);
        requestDTO.setUnidad("unidad");
    }

    @Test
    @DisplayName("Crear ingrediente con nombre único - debe retornar IngredienteDTO")
    void testCrear_nombreUnico_retornaDTO() {
        when(repository.findByNombre("Pan de completo")).thenReturn(Optional.empty());
        when(mapper.toEntity(requestDTO)).thenReturn(ingrediente);
        when(repository.save(ingrediente)).thenReturn(ingrediente);
        when(mapper.toDto(ingrediente)).thenReturn(ingredienteDTO);

        IngredienteDTO resultado = service.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("Pan de completo", resultado.getNombre());
        verify(repository, times(1)).save(ingrediente);
    }

    @Test
    @DisplayName("Obtener ingrediente por ID existente - debe retornar IngredienteDTO")
    void testObtenerPorId_existente_retornaDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(ingrediente));
        when(mapper.toDto(ingrediente)).thenReturn(ingredienteDTO);

        IngredienteDTO resultado = service.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Obtener ingrediente por ID inexistente - debe lanzar ResourceNotFoundException")
    void testObtenerPorId_noExiste_lanzaException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    @DisplayName("Listar todos los ingredientes - debe retornar lista")
    void testListarTodos_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(ingrediente));
        when(mapper.toDto(ingrediente)).thenReturn(ingredienteDTO);

        List<IngredienteDTO> resultado = service.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Eliminar ingrediente existente - debe llamar deleteById")
    void testEliminar_existente_eliminaCorrectamente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar ingrediente inexistente - debe lanzar ResourceNotFoundException")
    void testEliminar_noExiste_lanzaException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.eliminar(99L));
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Descontar stock con cantidad válida - debe reducir stock")
    void testDescontarStock_cantidadValida_reduceStock() {
        when(repository.findById(1L)).thenReturn(Optional.of(ingrediente));

        service.descontarStock(1L, 10);

        assertEquals(90, ingrediente.getStock());
        verify(repository, times(1)).save(ingrediente);
    }

    @Test
    @DisplayName("Descontar stock insuficiente - debe lanzar BusinessException")
    void testDescontarStock_insuficiente_lanzaException() {
        when(repository.findById(1L)).thenReturn(Optional.of(ingrediente));

        assertThrows(BusinessException.class, () -> service.descontarStock(1L, 200));
    }

    @Test
    @DisplayName("Aumentar stock - debe incrementar stock")
    void testAumentarStock_incrementaCorrectamente() {
        when(repository.findById(1L)).thenReturn(Optional.of(ingrediente));

        service.aumentarStock(1L, 50);

        assertEquals(150, ingrediente.getStock());
        verify(repository, times(1)).save(ingrediente);
    }
}
