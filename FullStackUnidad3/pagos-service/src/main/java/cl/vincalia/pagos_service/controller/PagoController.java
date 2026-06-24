package cl.vincalia.pagos_service.controller;

import cl.vincalia.pagos_service.dto.PagoDTO;
import cl.vincalia.pagos_service.dto.PagoRequestDTO;
import cl.vincalia.pagos_service.service.PagoService;
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
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Gestión de pagos del sistema")
public class PagoController {

    private final PagoService service;

    @Operation(summary = "Registrar un nuevo pago")
    @PostMapping
    public ResponseEntity<PagoDTO> crear(@Valid @RequestBody PagoRequestDTO request) {
        return new ResponseEntity<>(service.crearPago(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener pago por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> obtenerPorId(
            @Parameter(description = "ID del pago") @PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Listar todos los pagos")
    @GetMapping
    public ResponseEntity<List<PagoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Actualizar estado de un pago")
    @PutMapping("/{id}/estado")
    public ResponseEntity<PagoDTO> actualizarEstado(
            @Parameter(description = "ID del pago") @PathVariable Long id,
            @Parameter(description = "Nuevo estado: PENDIENTE, COMPLETADO, FALLIDO, REEMBOLSADO") @RequestParam String estado) {
        return ResponseEntity.ok(service.actualizarEstado(id, estado));
    }

    @Operation(summary = "Reportes: pagos por pedido")
    @GetMapping("/reportes/pedido/{pedidoId}")
    public ResponseEntity<List<PagoDTO>> getByPedido(
            @Parameter(description = "ID del pedido") @PathVariable Long pedidoId) {
        return ResponseEntity.ok(service.findByPedido(pedidoId));
    }

    @Operation(summary = "Reportes: pagos por estado")
    @GetMapping("/reportes/estado")
    public ResponseEntity<List<PagoDTO>> getByEstado(
            @Parameter(description = "Estado: PENDIENTE, COMPLETADO, FALLIDO, REEMBOLSADO") @RequestParam String estado) {
        return ResponseEntity.ok(service.findByEstado(estado));
    }

    @Operation(summary = "Reportes: pagos por método de pago")
    @GetMapping("/reportes/metodo")
    public ResponseEntity<List<PagoDTO>> getByMetodoPago(
            @Parameter(description = "Método: EFECTIVO, TARJETA_CREDITO, TARJETA_DEBITO, TRANSFERENCIA, OTRO") @RequestParam String metodoPago) {
        return ResponseEntity.ok(service.findByMetodoPago(metodoPago));
    }

    @Operation(summary = "Reportes: pagos por rango de fechas")
    @GetMapping("/reportes/fechas")
    public ResponseEntity<List<PagoDTO>> getByFechas(
            @Parameter(description = "Fecha inicio (ISO)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @Parameter(description = "Fecha fin (ISO)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(service.findByFechaRange(inicio, fin));
    }

    @Operation(summary = "Reportes: pagos por rango de monto")
    @GetMapping("/reportes/monto")
    public ResponseEntity<List<PagoDTO>> getByMontoRange(
            @Parameter(description = "Monto mínimo") @RequestParam(required = false) Double min,
            @Parameter(description = "Monto máximo") @RequestParam(required = false) Double max) {
        return ResponseEntity.ok(service.findByMontoRange(min, max));
    }
}
