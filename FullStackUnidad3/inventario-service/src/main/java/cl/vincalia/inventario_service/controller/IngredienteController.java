package cl.vincalia.inventario_service.controller;

import cl.vincalia.inventario_service.dto.IngredienteDTO;
import cl.vincalia.inventario_service.dto.IngredienteRequestDTO;
import cl.vincalia.inventario_service.service.IngredienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
@RequiredArgsConstructor
public class IngredienteController {

    private final IngredienteService service;

    @PostMapping
    public ResponseEntity<IngredienteDTO> crear(@Valid @RequestBody IngredienteRequestDTO request) {
        return new ResponseEntity<>(service.crear(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredienteDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<IngredienteDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredienteDTO> actualizar(@PathVariable Long id, @Valid @RequestBody IngredienteRequestDTO request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/descontar-stock")
    public ResponseEntity<Void> descontarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        service.descontarStock(id, cantidad);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/aumentar-stock")
    public ResponseEntity<Void> aumentarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        service.aumentarStock(id, cantidad);
        return ResponseEntity.ok().build();
    }

    // REPORTES
    @GetMapping("/reportes/disponibles")
    public ResponseEntity<List<IngredienteDTO>> getDisponibles() {
        return ResponseEntity.ok(service.findDisponibles());
    }

    @GetMapping("/reportes/stock-bajo")
    public ResponseEntity<List<IngredienteDTO>> getStockBajo(@RequestParam Integer limite) {
        return ResponseEntity.ok(service.findStockBajo(limite));
    }

    @GetMapping("/reportes/rango-precio")
    public ResponseEntity<List<IngredienteDTO>> getByPrecioRange(
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max) {
        return ResponseEntity.ok(service.findByPrecioRange(min, max));
    }
}
