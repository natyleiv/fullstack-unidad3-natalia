package cl.vincalia.inventario_service.service;

import cl.vincalia.inventario_service.clients.ProductoFeignClient;
import cl.vincalia.inventario_service.dto.IngredienteDTO;
import cl.vincalia.inventario_service.dto.IngredienteRequestDTO;
import cl.vincalia.inventario_service.exception.BusinessException;
import cl.vincalia.inventario_service.exception.ResourceNotFoundException;
import cl.vincalia.inventario_service.mapper.IngredienteMapper;
import cl.vincalia.inventario_service.model.Ingrediente;
import cl.vincalia.inventario_service.repository.IngredienteRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredienteService {

    private final IngredienteRepository repository;
    private final IngredienteMapper mapper;
    private final ProductoFeignClient productoClient;

    @Transactional
    public IngredienteDTO crear(IngredienteRequestDTO request) {
        if (repository.findByNombre(request.getNombre()).isPresent()) {
            throw new BusinessException("Ya existe un ingrediente con nombre: " + request.getNombre());
        }
        validarProductoAsociado(request.getProductoId());
        Ingrediente ingrediente = mapper.toEntity(request);
        ingrediente = repository.save(ingrediente);
        return mapper.toDto(ingrediente);
    }

    private void validarProductoAsociado(Long productoId) {
        if (productoId == null) return;
        try {
            productoClient.obtenerProducto(productoId);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("No existe producto con id: " + productoId);
        } catch (FeignException e) {
            throw new BusinessException("Error al comunicarse con el servicio de catálogo: " + e.getMessage());
        }
    }

    public IngredienteDTO obtenerPorId(Long id) {
        Ingrediente ingrediente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado con id: " + id));
        return mapper.toDto(ingrediente);
    }

    public List<IngredienteDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public IngredienteDTO actualizar(Long id, IngredienteRequestDTO request) {
        Ingrediente existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado"));

        if (!existing.getNombre().equalsIgnoreCase(request.getNombre()) &&
                repository.findByNombre(request.getNombre()).isPresent()) {
            throw new BusinessException("Ya existe otro ingrediente con nombre: " + request.getNombre());
        }
        validarProductoAsociado(request.getProductoId());

        mapper.updateEntity(request, existing);
        return mapper.toDto(repository.save(existing));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Ingrediente no encontrado");
        }
        repository.deleteById(id);
    }

    @Transactional
    public void descontarStock(Long id, Integer cantidad) {
        Ingrediente ingrediente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado"));
        if (ingrediente.getStock() < cantidad) {
            throw new BusinessException("Stock insuficiente para " + ingrediente.getNombre() + ". Disponible: " + ingrediente.getStock());
        }
        ingrediente.setStock(ingrediente.getStock() - cantidad);
        repository.save(ingrediente);
    }

    @Transactional
    public void aumentarStock(Long id, Integer cantidad) {
        Ingrediente ingrediente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado"));
        ingrediente.setStock(ingrediente.getStock() + cantidad);
        repository.save(ingrediente);
    }

    // ========== REPORTES ==========
    public List<IngredienteDTO> findDisponibles() {
        return repository.findByDisponibleTrue().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<IngredienteDTO> findStockBajo(Integer limite) {
        return repository.findByStockLessThan(limite).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<IngredienteDTO> findByPrecioRange(Double min, Double max) {
        if (min == null) min = 0.0;
        if (max == null) max = Double.MAX_VALUE;
        return repository.findByPrecioBetween(min, max).stream().map(mapper::toDto).collect(Collectors.toList());
    }
}