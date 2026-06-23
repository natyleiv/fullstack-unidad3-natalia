package cl.vincalia.clientes_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "cupones-service")
public interface CuponFeignClient {

    @GetMapping("/api/cupones/validar/{codigo}")
    Boolean validarCupon(@PathVariable("codigo") String codigo);

    @GetMapping("/api/cupones/descuento/{codigo}")
    Double obtenerDescuento(@PathVariable("codigo") String codigo);

    @PostMapping("/api/cupones/usar/{codigo}")
    void marcarCuponUsado(@PathVariable("codigo") String codigo);
}