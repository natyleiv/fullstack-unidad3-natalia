package cl.vincalia.pagos_service.clients;

import cl.vincalia.pagos_service.dto.PedidoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pedido-service")
public interface PedidoFeignClient {
    @GetMapping("/api/pedidos/{id}")
    PedidoDTO obtenerPedidoPorId(@PathVariable("id") Long id);
}