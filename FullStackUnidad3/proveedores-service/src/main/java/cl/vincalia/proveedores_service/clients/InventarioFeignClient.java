package cl.vincalia.proveedores_service.clients;


import cl.vincalia.proveedores_service.dto.IngredienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "inventario-service")
public interface InventarioFeignClient {
    @GetMapping("/api/ingredientes/reportes/proveedor")
    List<IngredienteDTO> getIngredientesByProveedor(@RequestParam("proveedorId") Long proveedorId);
}