package cl.vincalia.catalogo_service.mapper;

import cl.vincalia.catalogo_service.dto.ProductoDTO;
import cl.vincalia.catalogo_service.dto.ProductoRequestDTO;
import cl.vincalia.catalogo_service.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoDTO toDto(Producto producto) {
        if (producto == null) return null;
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setCategoria(producto.getCategoria());
        dto.setDisponible(producto.getDisponible());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setStock(producto.getStock());
        return dto;
    }

    public Producto toEntity(ProductoRequestDTO dto) {
        if (dto == null) return null;
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setCategoria(dto.getCategoria());
        producto.setDisponible(dto.getDisponible() != null ? dto.getDisponible() : true);
        producto.setStock(dto.getStock() != null ? dto.getStock() : 0);
        return producto;
    }

    public void updateEntity(ProductoRequestDTO dto, Producto producto) {
        if (dto.getNombre() != null) producto.setNombre(dto.getNombre());
        if (dto.getDescripcion() != null) producto.setDescripcion(dto.getDescripcion());
        if (dto.getPrecio() != null) producto.setPrecio(dto.getPrecio());
        if (dto.getCategoria() != null) producto.setCategoria(dto.getCategoria());
        if (dto.getDisponible() != null) producto.setDisponible(dto.getDisponible());
        if (dto.getStock() != null) producto.setStock(dto.getStock());
    }
}