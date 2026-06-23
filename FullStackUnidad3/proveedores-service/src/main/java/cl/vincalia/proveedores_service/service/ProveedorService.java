package cl.vincalia.proveedores_service.service;

import cl.vincalia.proveedores_service.dto.ProveedorDTO;
import cl.vincalia.proveedores_service.dto.ProveedorRequestDTO;
import cl.vincalia.proveedores_service.exception.DuplicateResourceException;
import cl.vincalia.proveedores_service.exception.ResourceNotFoundException;
import cl.vincalia.proveedores_service.mapper.ProveedorMapper;
import cl.vincalia.proveedores_service.model.Proveedor;
import cl.vincalia.proveedores_service.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository repository;
    private final ProveedorMapper mapper;

    @Transactional
    public ProveedorDTO crear(ProveedorRequestDTO request) {
        if (repository.findByRut(request.getRut()).isPresent()) {
            throw new DuplicateResourceException("Ya existe un proveedor con RUT: " + request.getRut());
        }
        if (repository.findByEmailContacto(request.getEmailContacto()).isPresent()) {
            throw new DuplicateResourceException("Ya existe un proveedor con email: " + request.getEmailContacto());
        }
        Proveedor proveedor = mapper.toEntity(request);
        return mapper.toDto(repository.save(proveedor));
    }

    public ProveedorDTO obtenerPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
    }

    public List<ProveedorDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ProveedorDTO actualizar(Long id, ProveedorRequestDTO request) {
        Proveedor existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));

        if (!existing.getRut().equals(request.getRut()) &&
                repository.findByRut(request.getRut()).isPresent()) {
            throw new DuplicateResourceException("Ya existe otro proveedor con RUT: " + request.getRut());
        }
        if (!existing.getEmailContacto().equalsIgnoreCase(request.getEmailContacto()) &&
                repository.findByEmailContacto(request.getEmailContacto()).isPresent()) {
            throw new DuplicateResourceException("Ya existe otro proveedor con email: " + request.getEmailContacto());
        }

        mapper.updateEntity(request, existing);
        return mapper.toDto(repository.save(existing));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Proveedor no encontrado");
        }
        repository.deleteById(id);
    }

    // ========== REPORTES ==========
    public List<ProveedorDTO> findActivos() {
        return repository.findByActivoTrue().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public ProveedorDTO findByRut(String rut) {
        return repository.findByRut(rut)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con RUT: " + rut));
    }

    public List<ProveedorDTO> findByRubro(String rubro) {
        return repository.findByRubro(rubro).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<ProveedorDTO> findByRazonSocialContaining(String nombre) {
        return repository.findByRazonSocialContainingIgnoreCase(nombre).stream().map(mapper::toDto).collect(Collectors.toList());
    }
}