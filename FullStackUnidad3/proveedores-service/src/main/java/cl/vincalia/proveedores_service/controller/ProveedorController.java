package cl.vincalia.proveedores_service.controller;

import cl.vincalia.proveedores_service.dto.ProveedorDTO;
import cl.vincalia.proveedores_service.dto.ProveedorRequestDTO;
import cl.vincalia.proveedores_service.service.ProveedorService;
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
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@Tag(name = "Proveedores", description = "Gestión de proveedores del sistema")
public class ProveedorController {

    private final ProveedorService service;

    @Operation(summary = "Crear un nuevo proveedor")
    @PostMapping
    public ResponseEntity<ProveedorDTO> crear(@Valid @RequestBody ProveedorRequestDTO request) {
        return new ResponseEntity<>(service.crear(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener proveedor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDTO> obtenerPorId(
            @Parameter(description = "ID del proveedor") @PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Listar todos los proveedores")
    @GetMapping
    public ResponseEntity<List<ProveedorDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Actualizar un proveedor existente")
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDTO> actualizar(
            @Parameter(description = "ID del proveedor") @PathVariable Long id,
            @Valid @RequestBody ProveedorRequestDTO request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @Operation(summary = "Eliminar un proveedor por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del proveedor") @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reportes: proveedores activos")
    @GetMapping("/reportes/activos")
    public ResponseEntity<List<ProveedorDTO>> getActivos() {
        return ResponseEntity.ok(service.findActivos());
    }

    @Operation(summary = "Reportes: buscar proveedor por RUT")
    @GetMapping("/reportes/rut")
    public ResponseEntity<ProveedorDTO> getByRut(
            @Parameter(description = "RUT del proveedor") @RequestParam String rut) {
        return ResponseEntity.ok(service.findByRut(rut));
    }

    @Operation(summary = "Reportes: proveedores por rubro")
    @GetMapping("/reportes/rubro")
    public ResponseEntity<List<ProveedorDTO>> getByRubro(
            @Parameter(description = "Rubro del proveedor") @RequestParam String rubro) {
        return ResponseEntity.ok(service.findByRubro(rubro));
    }

    @Operation(summary = "Reportes: buscar por razón social")
    @GetMapping("/reportes/razon-social")
    public ResponseEntity<List<ProveedorDTO>> getByRazonSocial(
            @Parameter(description = "Nombre o parte del nombre") @RequestParam String nombre) {
        return ResponseEntity.ok(service.findByRazonSocialContaining(nombre));
    }
}
