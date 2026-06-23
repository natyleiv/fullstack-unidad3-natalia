package cl.vincalia.catalogo_service;

import cl.vincalia.catalogo_service.dto.ProductoDTO;
import cl.vincalia.catalogo_service.dto.ProductoRequestDTO;
import cl.vincalia.catalogo_service.exception.DuplicateResourceException;
import cl.vincalia.catalogo_service.exception.ResourceNotFoundException;
import cl.vincalia.catalogo_service.mapper.ProductoMapper;
import cl.vincalia.catalogo_service.model.Producto;
import cl.vincalia.catalogo_service.repository.ProductoRepository;
import cl.vincalia.catalogo_service.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para ProductoService")
class ProductoServiceTest {

    @Mock private ProductoRepository repository;
    @Mock private ProductoMapper mapper;
    @InjectMocks private ProductoService service;

    private Producto producto;
    private ProductoDTO productoDTO;
    private ProductoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Hamburguesa Clásica");
        producto.setDescripcion("Carne, lechuga, tomate");
        producto.setPrecio(5990.0);
        producto.setCategoria("Comida");
        producto.setDisponible(true);
        producto.setStock(10);

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
    @DisplayName("Crear producto con nombre único - debe retornar ProductoDTO")
    void testCrear_nombreUnico_retornaDTO() {
        when(repository.findByNombreContainingIgnoreCase("Hamburguesa Clásica")).thenReturn(List.of());
        when(mapper.toEntity(requestDTO)).thenReturn(producto);
        when(repository.save(producto)).thenReturn(producto);
        when(mapper.toDto(producto)).thenReturn(productoDTO);

        ProductoDTO resultado = service.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("Hamburguesa Clásica", resultado.getNombre());
        verify(repository, times(1)).save(producto);
    }

    @Test
    @DisplayName("Crear producto con nombre duplicado - debe lanzar DuplicateResourceException")
    void testCrear_nombreDuplicado_lanzaException() {
        when(repository.findByNombreContainingIgnoreCase("Hamburguesa Clásica")).thenReturn(List.of(producto));

        assertThrows(DuplicateResourceException.class, () -> service.crear(requestDTO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Obtener producto por id existente - debe retornar ProductoDTO")
    void testObtenerPorId_existente_retornaDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(producto));
        when(mapper.toDto(producto)).thenReturn(productoDTO);

        ProductoDTO resultado = service.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Obtener producto por id inexistente - debe lanzar ResourceNotFoundException")
    void testObtenerPorId_noExiste_lanzaException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    @DisplayName("Listar todos los productos - debe retornar lista")
    void testListarTodos_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(producto));
        when(mapper.toDto(producto)).thenReturn(productoDTO);

        List<ProductoDTO> resultado = service.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Eliminar producto existente - debe llamar deleteById")
    void testEliminar_existente_eliminaCorrectamente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Vender producto con stock suficiente - debe descontar stock")
    void testVender_stockSuficiente_descuentaStock() {
        when(repository.findById(1L)).thenReturn(Optional.of(producto));
        when(repository.save(any())).thenReturn(producto);

        service.venderProducto(1L, 3);

        assertEquals(7, producto.getStock());
    }

    @Test
    @DisplayName("Vender producto sin stock - debe lanzar RuntimeException")
    void testVender_sinStock_lanzaException() {
        producto.setStock(2);
        when(repository.findById(1L)).thenReturn(Optional.of(producto));

        assertThrows(RuntimeException.class, () -> service.venderProducto(1L, 5));
    }
}