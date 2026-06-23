package cl.vincalia.catalogo_service.service;

import cl.vincalia.catalogo_service.dto.ProductoDTO;
import cl.vincalia.catalogo_service.dto.ProductoRequestDTO;
import cl.vincalia.catalogo_service.exception.DuplicateResourceException;
import cl.vincalia.catalogo_service.exception.ResourceNotFoundException;
import cl.vincalia.catalogo_service.mapper.ProductoMapper;
import cl.vincalia.catalogo_service.model.Producto;
import cl.vincalia.catalogo_service.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository repository;
    private final ProductoMapper mapper;

    @Transactional
    public ProductoDTO crear(ProductoRequestDTO request) {
        if (repository.findByNombreContainingIgnoreCase(request.getNombre()).stream()
                .anyMatch(p -> p.getNombre().equalsIgnoreCase(request.getNombre()))) {
            throw new DuplicateResourceException("Ya existe un producto con nombre: " + request.getNombre());
        }

        Producto producto = mapper.toEntity(request);
        producto = repository.save(producto);
        return mapper.toDto(producto);
    }

    public ProductoDTO obtenerPorId(Long id) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return mapper.toDto(producto);
    }

    public List<ProductoDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ProductoDTO actualizar(Long id, ProductoRequestDTO request) {
        Producto existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        if (!existing.getNombre().equalsIgnoreCase(request.getNombre()) &&
                repository.findByNombreContainingIgnoreCase(request.getNombre()).stream()
                        .anyMatch(p -> p.getNombre().equalsIgnoreCase(request.getNombre()))) {
            throw new DuplicateResourceException("Ya existe otro producto con nombre: " + request.getNombre());
        }

        mapper.updateEntity(request, existing);
        return mapper.toDto(repository.save(existing));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }
        repository.deleteById(id);
    }

    // ========== REPORTES ==========
    public List<ProductoDTO> findDisponibles() {
        return repository.findByDisponibleTrue().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<ProductoDTO> findByCategoria(String categoria) {
        return repository.findByCategoria(categoria).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<ProductoDTO> findByNombreContaining(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<ProductoDTO> findByPrecioRange(Double min, Double max) {
        if (min == null) min = 0.0;
        if (max == null) max = Double.MAX_VALUE;
        return repository.findByPrecioBetween(min, max).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<ProductoDTO> findStockBajo(Integer limite) {
        return repository.findByStockLessThan(limite).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    // ========== MÉTODOS PARA TESTING (UNITARIOS) ==========

    @Transactional
    public Double calcularTotal(List<Long> ids) {
        Double total = 0.0;
        for (Long id : ids) {
            Producto p = repository.findById(id).orElse(null);
            if (p != null) {
                total += p.getPrecio();
            }
        }
        return total;
    }

    @Transactional
    public void venderProducto(Long id, Integer cantidad) {
        Producto p = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (p.getStock() < cantidad) {
            throw new RuntimeException("No hay stock suficiente");
        }

        p.setStock(p.getStock() - cantidad);
        repository.save(p);
    }
}