package cl.vincalia.pedidos_service.clients;

import cl.vincalia.pedidos_service.dto.ClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clientes-service")
public interface ClienteFeignClient {
    @GetMapping("/api/clientes/{id}")
    ClienteDTO obtenerClientePorId(@PathVariable("id") Long id);
}