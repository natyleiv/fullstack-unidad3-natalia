package cl.vincalia.delivery_service.controller;

import cl.vincalia.delivery_service.dto.EntregaDTO;
import cl.vincalia.delivery_service.dto.EntregaRequestDTO;
import cl.vincalia.delivery_service.service.EntregaService;
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
@RequestMapping("/api/entregas")
@RequiredArgsConstructor
@Tag(name = "Entregas", description = "Gestión de entregas del sistema")
public class EntregaController {

    private final EntregaService service;

    @Operation(summary = "Crear una nueva entrega")
    @PostMapping
    public ResponseEntity<EntregaDTO> crear(@Valid @RequestBody EntregaRequestDTO request) {
        return new ResponseEntity<>(service.crearEntrega(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener entrega por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntregaDTO> obtenerPorId(
            @Parameter(description = "ID de la entrega") @PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Listar todas las entregas")
    @GetMapping
    public ResponseEntity<List<EntregaDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Actualizar estado de una entrega")
    @PutMapping("/{id}/estado")
    public ResponseEntity<EntregaDTO> actualizarEstado(
            @Parameter(description = "ID de la entrega") @PathVariable Long id,
            @Parameter(description = "Nuevo estado: PENDIENTE, EN_CAMINO, ENTREGADO, CANCELADO") @RequestParam String estado) {
        return ResponseEntity.ok(service.actualizarEstado(id, estado));
    }

    @Operation(summary = "Eliminar una entrega")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reportes: entregas por pedido")
    @GetMapping("/reportes/pedido/{pedidoId}")
    public ResponseEntity<List<EntregaDTO>> getByPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(service.findByPedido(pedidoId));
    }

    @Operation(summary = "Reportes: entregas por estado")
    @GetMapping("/reportes/estado")
    public ResponseEntity<List<EntregaDTO>> getByEstado(@RequestParam String estado) {
        return ResponseEntity.ok(service.findByEstado(estado));
    }

    @Operation(summary = "Reportes: entregas por repartidor")
    @GetMapping("/reportes/repartidor")
    public ResponseEntity<List<EntregaDTO>> getByRepartidor(@RequestParam String repartidor) {
        return ResponseEntity.ok(service.findByRepartidor(repartidor));
    }

    @Operation(summary = "Reportes: entregas por rango de fecha de creacion")
    @GetMapping("/reportes/fecha-creacion")
    public ResponseEntity<List<EntregaDTO>> getByFechaCreacion(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(service.findByFechaCreacionRange(inicio, fin));
    }

    @Operation(summary = "Reportes: entregas por rango de fecha estimada")
    @GetMapping("/reportes/fecha-estimada")
    public ResponseEntity<List<EntregaDTO>> getByFechaEstimada(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(service.findByFechaEstimadaRange(inicio, fin));
    }
}
