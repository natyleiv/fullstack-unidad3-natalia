package cl.vincalia.catalogo_service.controller;

import cl.vincalia.catalogo_service.dto.ProductoDTO;
import cl.vincalia.catalogo_service.dto.ProductoRequestDTO;
import cl.vincalia.catalogo_service.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos",
        description = "Endpoints para la gestión, mantenimiento y reportes de los productos del catálogo")
public class ProductoController {

    private final ProductoService service;

    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Registra un producto en la base de datos del catálogo.")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    public ResponseEntity<ProductoDTO> crear(
            @Valid @RequestBody ProductoRequestDTO request) {
        return new ResponseEntity<>(service.crear(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Busca y retorna los detalles de un producto específico mediante su ID.")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    public ResponseEntity<ProductoDTO> obtenerPorId(
            @Parameter(description = "ID único del producto") @PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos los productos", description = "Retorna una lista completa de todos los productos registrados, sin filtros.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito")
    public ResponseEntity<List<ProductoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Modifica los datos de un producto existente según su ID.")
    @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado para actualizar")
    public ResponseEntity<ProductoDTO> actualizar(
            @Parameter(description = "ID del producto a modificar") @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema de forma permanente.")
    @ApiResponse(responseCode = "204", description = "Producto eliminado con éxito")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del producto a eliminar") @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ========== REPORTES ==========

    @GetMapping("/reportes/disponibles")
    @Operation(summary = "Reporte: Productos Disponibles", description = "Filtra y lista únicamente los productos que cuentan con stock disponible (mayor a cero).")
    public ResponseEntity<List<ProductoDTO>> getDisponibles() {
        return ResponseEntity.ok(service.findDisponibles());
    }

    @GetMapping("/reportes/categoria")
    @Operation(summary = "Reporte: Búsqueda por Categoría", description = "Obtiene una lista de productos que pertenecen a una categoría específica.")
    public ResponseEntity<List<ProductoDTO>> getByCategoria(
            @Parameter(description = "Nombre exacto de la categoría (ej: 'Teclados')") @RequestParam String categoria) {
        return ResponseEntity.ok(service.findByCategoria(categoria));
    }

    @GetMapping("/reportes/nombre")
    @Operation(summary = "Reporte: Búsqueda por Nombre", description = "Realiza una búsqueda parcial para encontrar productos que contengan la palabra ingresada en su nombre.")
    public ResponseEntity<List<ProductoDTO>> getByNombre(
            @Parameter(description = "Palabra o fragmento del nombre a buscar") @RequestParam String nombre) {
        return ResponseEntity.ok(service.findByNombreContaining(nombre));
    }

    @GetMapping("/reportes/rango-precio")
    @Operation(summary = "Reporte: Filtrar por Precio", description = "Obtiene los productos cuyo precio se encuentre dentro del rango especificado (mínimo y máximo).")
    public ResponseEntity<List<ProductoDTO>> getByPrecioRange(
            @Parameter(description = "Precio mínimo", example = "10000") @RequestParam(required = false) Double min,
            @Parameter(description = "Precio máximo", example = "50000") @RequestParam(required = false) Double max) {
        return ResponseEntity.ok(service.findByPrecioRange(min, max));
    }

    @GetMapping("/reportes/stock-bajo")
    @Operation(summary = "Reporte: Alerta de Stock Bajo", description = "Lista los productos cuyo inventario actual sea menor o igual al límite indicado.")
    public ResponseEntity<List<ProductoDTO>> getStockBajo(
            @Parameter(description = "Límite máximo de stock para considerar la alerta", example = "5") @RequestParam Integer limite) {
        return ResponseEntity.ok(service.findStockBajo(limite));
    }
}