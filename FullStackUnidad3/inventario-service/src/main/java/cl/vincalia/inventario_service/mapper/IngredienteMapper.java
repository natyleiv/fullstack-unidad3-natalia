package cl.vincalia.inventario_service.mapper;

import cl.vincalia.inventario_service.dto.IngredienteDTO;
import cl.vincalia.inventario_service.dto.IngredienteRequestDTO;
import cl.vincalia.inventario_service.model.Ingrediente;
import org.springframework.stereotype.Component;

@Component
public class IngredienteMapper {

    public IngredienteDTO toDto(Ingrediente ingrediente) {
        if (ingrediente == null) return null;
        IngredienteDTO dto = new IngredienteDTO();
        dto.setId(ingrediente.getId());
        dto.setNombre(ingrediente.getNombre());
        dto.setStock(ingrediente.getStock());
        dto.setPrecio(ingrediente.getPrecio());
        dto.setDisponible(ingrediente.getDisponible());
        dto.setFechaCreacion(ingrediente.getFechaCreacion());
        dto.setUnidad(ingrediente.getUnidad());
        return dto;
    }

    public Ingrediente toEntity(IngredienteRequestDTO dto) {
        if (dto == null) return null;
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNombre(dto.getNombre());
        ingrediente.setStock(dto.getStock() != null ? dto.getStock() : 0);
        ingrediente.setPrecio(dto.getPrecio());
        ingrediente.setDisponible(dto.getDisponible() != null ? dto.getDisponible() : true);
        ingrediente.setUnidad(dto.getUnidad());
        return ingrediente;
    }

    public void updateEntity(IngredienteRequestDTO dto, Ingrediente ingrediente) {
        if (dto.getNombre() != null) ingrediente.setNombre(dto.getNombre());
        if (dto.getStock() != null) ingrediente.setStock(dto.getStock());
        if (dto.getPrecio() != null) ingrediente.setPrecio(dto.getPrecio());
        if (dto.getDisponible() != null) ingrediente.setDisponible(dto.getDisponible());
        if (dto.getUnidad() != null) ingrediente.setUnidad(dto.getUnidad());
    }
}