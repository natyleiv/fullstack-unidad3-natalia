package cl.vincalia.proveedores_service.controller;

import cl.vincalia.proveedores_service.dto.ProveedorDTO;
import cl.vincalia.proveedores_service.dto.ProveedorRequestDTO;
import cl.vincalia.proveedores_service.service.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService service;

    @PostMapping
    public ResponseEntity<ProveedorDTO> crear(@Valid @RequestBody ProveedorRequestDTO request) {
        return new ResponseEntity<>(service.crear(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ProveedorDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorRequestDTO request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // REPORTES
    @GetMapping("/reportes/activos")
    public ResponseEntity<List<ProveedorDTO>> getActivos() {
        return ResponseEntity.ok(service.findActivos());
    }

    @GetMapping("/reportes/rut")
    public ResponseEntity<ProveedorDTO> getByRut(@RequestParam String rut) {
        return ResponseEntity.ok(service.findByRut(rut));
    }

    @GetMapping("/reportes/rubro")
    public ResponseEntity<List<ProveedorDTO>> getByRubro(@RequestParam String rubro) {
        return ResponseEntity.ok(service.findByRubro(rubro));
    }

    @GetMapping("/reportes/razon-social")
    public ResponseEntity<List<ProveedorDTO>> getByRazonSocial(@RequestParam String nombre) {
        return ResponseEntity.ok(service.findByRazonSocialContaining(nombre));
    }
}