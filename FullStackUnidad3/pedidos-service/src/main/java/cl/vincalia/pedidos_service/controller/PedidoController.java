package cl.vincalia.pedidos_service.controller;

import cl.vincalia.pedidos_service.dto.PedidoDTO;
import cl.vincalia.pedidos_service.dto.PedidoRequestDTO;
import cl.vincalia.pedidos_service.service.PedidoService;
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
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Gestión de pedidos del sistema")
public class PedidoController {

    private final PedidoService service;

    @Operation(summary = "Crear un nuevo pedido")
    @PostMapping
    public ResponseEntity<PedidoDTO> crear(@Valid @RequestBody PedidoRequestDTO request) {
        return new ResponseEntity<>(service.crearPedido(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener pedido por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obtenerPorId(
            @Parameter(description = "ID del pedido") @PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Listar todos los pedidos")
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Actualizar el total de un pedido")
    @PutMapping("/{id}/total")
    public ResponseEntity<PedidoDTO> actualizarTotal(
            @Parameter(description = "ID del pedido") @PathVariable Long id,
            @Parameter(description = "Nuevo total") @RequestParam Double total) {
        return ResponseEntity.ok(service.actualizarTotal(id, total));
    }

    @Operation(summary = "Reportes: pedidos por cliente")
    @GetMapping("/reportes/cliente/{clienteId}")
    public ResponseEntity<List<PedidoDTO>> getByCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.findByCliente(clienteId));
    }

    @Operation(summary = "Reportes: pedidos por rango de fechas")
    @GetMapping("/reportes/fechas")
    public ResponseEntity<List<PedidoDTO>> getByFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(service.findByFechaRange(inicio, fin));
    }
}
