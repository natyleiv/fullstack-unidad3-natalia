package cl.vincalia.inventario_service.controller;

import cl.vincalia.inventario_service.dto.IngredienteDTO;
import cl.vincalia.inventario_service.dto.IngredienteRequestDTO;
import cl.vincalia.inventario_service.service.IngredienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
@RequiredArgsConstructor
@Tag(name = "Ingredientes", description = "Gestión de ingredientes e inventario del sistema")
public class IngredienteController {

    private final IngredienteService service;

    @Operation(summary = "Crear un nuevo ingrediente")
    @PostMapping
    public ResponseEntity<IngredienteDTO> crear(@Valid @RequestBody IngredienteRequestDTO request) {
        return new ResponseEntity<>(service.crear(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener ingrediente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<IngredienteDTO> obtenerPorId(
            @Parameter(description = "ID del ingrediente") @PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Listar todos los ingredientes")
    @GetMapping
    public ResponseEntity<List<IngredienteDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Actualizar un ingrediente existente")
    @PutMapping("/{id}")
    public ResponseEntity<IngredienteDTO> actualizar(
            @Parameter(description = "ID del ingrediente") @PathVariable Long id,
            @Valid @RequestBody IngredienteRequestDTO request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @Operation(summary = "Eliminar un ingrediente por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del ingrediente") @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Descontar stock de un ingrediente")
    @PutMapping("/{id}/descontar-stock")
    public ResponseEntity<Void> descontarStock(
            @Parameter(description = "ID del ingrediente") @PathVariable Long id,
            @Parameter(description = "Cantidad a descontar") @RequestParam Integer cantidad) {
        service.descontarStock(id, cantidad);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Aumentar stock de un ingrediente")
    @PutMapping("/{id}/aumentar-stock")
    public ResponseEntity<Void> aumentarStock(
            @Parameter(description = "ID del ingrediente") @PathVariable Long id,
            @Parameter(description = "Cantidad a aumentar") @RequestParam Integer cantidad) {
        service.aumentarStock(id, cantidad);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reportes: ingredientes disponibles")
    @GetMapping("/reportes/disponibles")
    public ResponseEntity<List<IngredienteDTO>> getDisponibles() {
        return ResponseEntity.ok(service.findDisponibles());
    }

    @Operation(summary = "Reportes: ingredientes con stock bajo")
    @GetMapping("/reportes/stock-bajo")
    public ResponseEntity<List<IngredienteDTO>> getStockBajo(
            @Parameter(description = "Límite de stock") @RequestParam Integer limite) {
        return ResponseEntity.ok(service.findStockBajo(limite));
    }

    @Operation(summary = "Reportes: ingredientes por rango de precio")
    @GetMapping("/reportes/rango-precio")
    public ResponseEntity<List<IngredienteDTO>> getByPrecioRange(
            @Parameter(description = "Precio mínimo") @RequestParam(required = false) Double min,
            @Parameter(description = "Precio máximo") @RequestParam(required = false) Double max) {
        return ResponseEntity.ok(service.findByPrecioRange(min, max));
    }
}
