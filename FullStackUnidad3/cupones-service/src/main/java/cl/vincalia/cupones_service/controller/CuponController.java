package cl.vincalia.cupones_service.controller;

import cl.vincalia.cupones_service.dto.CuponDTO;
import cl.vincalia.cupones_service.dto.CuponRequestDTO;
import cl.vincalia.cupones_service.service.CuponService;
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
@RequestMapping("/api/cupones")
@RequiredArgsConstructor
@Tag(name = "Cupones", description = "Gestión de cupones de descuento del sistema")
public class CuponController {

    private final CuponService service;

    @Operation(summary = "Crear un nuevo cupón de descuento")
    @PostMapping
    public ResponseEntity<CuponDTO> crear(@Valid @RequestBody CuponRequestDTO request) {
        return new ResponseEntity<>(service.crear(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener cupón por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CuponDTO> obtenerPorId(
            @Parameter(description = "ID del cupón") @PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Obtener cupón por código")
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CuponDTO> obtenerPorCodigo(
            @Parameter(description = "Código del cupón") @PathVariable String codigo) {
        return ResponseEntity.ok(service.obtenerPorCodigo(codigo));
    }

    @Operation(summary = "Listar todos los cupones")
    @GetMapping
    public ResponseEntity<List<CuponDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Actualizar un cupón existente")
    @PutMapping("/{id}")
    public ResponseEntity<CuponDTO> actualizar(
            @Parameter(description = "ID del cupón") @PathVariable Long id,
            @Valid @RequestBody CuponRequestDTO request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @Operation(summary = "Eliminar un cupón por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del cupón") @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Marcar un cupón como usado")
    @PutMapping("/usar/{codigo}")
    public ResponseEntity<Void> marcarUsado(
            @Parameter(description = "Código del cupón a marcar como usado") @PathVariable String codigo) {
        service.marcarUsado(codigo);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reportes: cupones activos")
    @GetMapping("/reportes/activos")
    public ResponseEntity<List<CuponDTO>> getActivos() {
        return ResponseEntity.ok(service.findActivos());
    }

    @Operation(summary = "Reportes: cupones no usados")
    @GetMapping("/reportes/no-usados")
    public ResponseEntity<List<CuponDTO>> getNoUsados() {
        return ResponseEntity.ok(service.findNoUsados());
    }

    @Operation(summary = "Reportes: cupones vencidos")
    @GetMapping("/reportes/vencidos")
    public ResponseEntity<List<CuponDTO>> getVencidos() {
        return ResponseEntity.ok(service.findVencidos());
    }

    @Operation(summary = "Reportes: cupones vigentes")
    @GetMapping("/reportes/vigentes")
    public ResponseEntity<List<CuponDTO>> getVigentes() {
        return ResponseEntity.ok(service.findVigentes());
    }
}
