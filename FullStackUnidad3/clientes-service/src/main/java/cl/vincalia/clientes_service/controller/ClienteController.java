package cl.vincalia.clientes_service.controller;

import cl.vincalia.clientes_service.dto.ClienteDTO;
import cl.vincalia.clientes_service.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Gestión de clientes del sistema")
public class ClienteController {

    private final ClienteService service;

    @Operation(summary = "Crear un nuevo cliente")
    @PostMapping
    public ResponseEntity<ClienteDTO> create(@Valid @RequestBody ClienteDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getById(
            @Parameter(description = "ID del cliente") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Listar todos los clientes")
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Actualizar un cliente existente")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> update(
            @Parameter(description = "ID del cliente") @PathVariable Long id,
            @Valid @RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Eliminar un cliente por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del cliente") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reportes: clientes activos")
    @GetMapping("/reportes/activos")
    public ResponseEntity<List<ClienteDTO>> getActivos() {
        return ResponseEntity.ok(service.findActivos());
    }

    @Operation(summary = "Reportes: buscar cliente por RUT")
    @GetMapping("/reportes/rut")
    public ResponseEntity<ClienteDTO> getByRut(
            @Parameter(description = "RUT del cliente") @RequestParam String rut) {
        return ResponseEntity.ok(service.findByRut(rut));
    }

    @Operation(summary = "Reportes: buscar cliente por email")
    @GetMapping("/reportes/email")
    public ResponseEntity<ClienteDTO> getByEmail(
            @Parameter(description = "Email del cliente") @RequestParam String email) {
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @Operation(summary = "Reportes: buscar clientes por nombre")
    @GetMapping("/reportes/nombre")
    public ResponseEntity<List<ClienteDTO>> getByNombre(
            @Parameter(description = "Nombre o parte del nombre") @RequestParam String nombre) {
        return ResponseEntity.ok(service.findByNombreContaining(nombre));
    }

    @Operation(summary = "Reportes: clientes por rango de fecha de registro")
    @GetMapping("/reportes/rango-fechas")
    public ResponseEntity<List<ClienteDTO>> getByFechaRegistro(
            @Parameter(description = "Fecha inicio (ISO)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @Parameter(description = "Fecha fin (ISO)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(service.findByFechaRegistroBetween(inicio, fin));
    }
}
