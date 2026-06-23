package cl.vincalia.clientes_service.controller;

import cl.vincalia.clientes_service.dto.ClienteDTO;
import cl.vincalia.clientes_service.service.ClienteService;
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
public class ClienteController {

    private final ClienteService service;

    @PostMapping
    public ResponseEntity<ClienteDTO> create(@Valid @RequestBody ClienteDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> update(@PathVariable Long id, @Valid @RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // REPORTES
    @GetMapping("/reportes/activos")
    public ResponseEntity<List<ClienteDTO>> getActivos() {
        return ResponseEntity.ok(service.findActivos());
    }

    @GetMapping("/reportes/rut")
    public ResponseEntity<ClienteDTO> getByRut(@RequestParam String rut) {
        return ResponseEntity.ok(service.findByRut(rut));
    }

    @GetMapping("/reportes/email")
    public ResponseEntity<ClienteDTO> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @GetMapping("/reportes/nombre")
    public ResponseEntity<List<ClienteDTO>> getByNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(service.findByNombreContaining(nombre));
    }

    @GetMapping("/reportes/rango-fechas")
    public ResponseEntity<List<ClienteDTO>> getByFechaRegistro(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(service.findByFechaRegistroBetween(inicio, fin));
    }
}