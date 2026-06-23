package cl.vincalia.cupones_service.controller;

import cl.vincalia.cupones_service.dto.CuponDTO;
import cl.vincalia.cupones_service.dto.CuponRequestDTO;
import cl.vincalia.cupones_service.service.CuponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cupones")
@RequiredArgsConstructor
public class CuponController {

    private final CuponService service;

    @PostMapping
    public ResponseEntity<CuponDTO> crear(@Valid @RequestBody CuponRequestDTO request) {
        return new ResponseEntity<>(service.crear(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuponDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CuponDTO> obtenerPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(service.obtenerPorCodigo(codigo));
    }

    @GetMapping
    public ResponseEntity<List<CuponDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuponDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CuponRequestDTO request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/usar/{codigo}")
    public ResponseEntity<Void> marcarUsado(@PathVariable String codigo) {
        service.marcarUsado(codigo);
        return ResponseEntity.ok().build();
    }

    // REPORTES
    @GetMapping("/reportes/activos")
    public ResponseEntity<List<CuponDTO>> getActivos() {
        return ResponseEntity.ok(service.findActivos());
    }

    @GetMapping("/reportes/no-usados")
    public ResponseEntity<List<CuponDTO>> getNoUsados() {
        return ResponseEntity.ok(service.findNoUsados());
    }

    @GetMapping("/reportes/vencidos")
    public ResponseEntity<List<CuponDTO>> getVencidos() {
        return ResponseEntity.ok(service.findVencidos());
    }

    @GetMapping("/reportes/vigentes")
    public ResponseEntity<List<CuponDTO>> getVigentes() {
        return ResponseEntity.ok(service.findVigentes());
    }
}