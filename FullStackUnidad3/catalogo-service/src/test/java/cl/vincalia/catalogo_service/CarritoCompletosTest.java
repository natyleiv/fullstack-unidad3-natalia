package cl.vincalia.catalogo_service;

import cl.vincalia.catalogo_service.model.Producto;
import cl.vincalia.catalogo_service.repository.ProductoRepository;
import cl.vincalia.catalogo_service.service.ProductoService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias para CatalogoService")
class CarritoCompletosTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    @DisplayName("Validación del Cálculo de Carrito")
    void testCalcularTotal() {
        // 1. ARRANGE (Preparar los datos de entrada - Enviando los 8 parámetros del modelo)
        // Orden: id, nombre, descripcion, precio, categoria, disponible, fechaCreacion, stock
        Producto italiano = new Producto(1L, "Completo Italiano", "Tradicional", 2500.0, "Comida", true, LocalDateTime.now(), 10);
        Producto bebida = new Producto(2L, "Bebida 350cc", "Desechable", 1200.0, "Bebidas", true, LocalDateTime.now(), 5);

        when(repository.findById(1L)).thenReturn(Optional.of(italiano));
        when(repository.findById(2L)).thenReturn(Optional.of(bebida));

        List<Long> carritoIds = List.of(1L, 2L);

        // 2. ACT (Ejecutar la operación real)
        Double resultadoReal = productoService.calcularTotal(carritoIds);

        // 3. ASSERT (Verificar que 2500 + 1200 sea obligatoriamente 3700.0)
        assertEquals(3700.0, resultadoReal, "¡Alerta! El cálculo del carrito de completos falló.");
    }

    @Test
    @DisplayName("Validación de Venta sin Stock")
    void testVenderSinStock() {
        // 1. ARRANGE (Preparar los datos de entrada - Enviando los 8 parámetros del modelo)
        Producto sinPalta = new Producto(1L, "Completo Italiano", "Sin stock", 2500.0, "Comida", true, LocalDateTime.now(), 0);

        when(repository.findById(1L)).thenReturn(Optional.of(sinPalta));

        // 2. ACT (Ejecutar la operación real)
        // 3. ASSERT (Verificar que lance excepción obligatoriamente)
        assertThrows(RuntimeException.class, () -> {
            productoService.venderProducto(1L, 1);
        }, "¡Alerta! El sistema permitió vender un completo sin palta (sin stock).");
    }
}