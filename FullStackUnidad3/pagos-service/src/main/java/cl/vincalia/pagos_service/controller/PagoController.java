package cl.vincalia.pagos_service.controller;

import cl.vincalia.pagos_service.dto.PagoDTO;
import cl.vincalia.pagos_service.dto.PagoRequestDTO;
import cl.vincalia.pagos_service.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService service;

    @PostMapping
    public ResponseEntity<PagoDTO> crear(@Valid @RequestBody PagoRequestDTO request) {
        return new ResponseEntity<>(service.crearPago(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PagoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<PagoDTO> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(service.actualizarEstado(id, estado));
    }

    // REPORTES
    @GetMapping("/reportes/pedido/{pedidoId}")
    public ResponseEntity<List<PagoDTO>> getByPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(service.findByPedido(pedidoId));
    }

    @GetMapping("/reportes/estado")
    public ResponseEntity<List<PagoDTO>> getByEstado(@RequestParam String estado) {
        return ResponseEntity.ok(service.findByEstado(estado));
    }

    @GetMapping("/reportes/metodo")
    public ResponseEntity<List<PagoDTO>> getByMetodoPago(@RequestParam String metodoPago) {
        return ResponseEntity.ok(service.findByMetodoPago(metodoPago));
    }

    @GetMapping("/reportes/fechas")
    public ResponseEntity<List<PagoDTO>> getByFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(service.findByFechaRange(inicio, fin));
    }

    @GetMapping("/reportes/monto")
    public ResponseEntity<List<PagoDTO>> getByMontoRange(
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max) {
        return ResponseEntity.ok(service.findByMontoRange(min, max));
    }
}