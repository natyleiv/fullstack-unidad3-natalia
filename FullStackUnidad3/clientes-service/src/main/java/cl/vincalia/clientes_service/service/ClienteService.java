package cl.vincalia.clientes_service.service;

import cl.vincalia.clientes_service.dto.ClienteDTO;
import cl.vincalia.clientes_service.exception.DuplicateResourceException;
import cl.vincalia.clientes_service.exception.ResourceNotFoundException;
import cl.vincalia.clientes_service.mapper.ClienteMapper;
import cl.vincalia.clientes_service.model.Cliente;
import cl.vincalia.clientes_service.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;

    @Transactional
    public ClienteDTO create(ClienteDTO dto) {
        // Validar RUT único
        if (repository.findByRut(dto.getRut()).isPresent()) {
            throw new DuplicateResourceException("Ya existe un cliente con RUT: " + dto.getRut());
        }
        // Validar email único
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Ya existe un cliente con email: " + dto.getEmail());
        }
        // Validar teléfono único (opcional, pero recomendable)
        if (repository.findByTelefono(dto.getTelefono()).isPresent()) {
            throw new DuplicateResourceException("Ya existe un cliente con teléfono: " + dto.getTelefono());
        }
        Cliente cliente = mapper.toEntity(dto);
        cliente.setFechaRegistro(LocalDateTime.now());
        cliente.setActivo(true);
        return mapper.toDto(repository.save(cliente));
    }

    @Transactional
    public ClienteDTO update(Long id, ClienteDTO dto) {
        Cliente existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        // Si cambia el RUT, validar que no esté ya usado por otro cliente
        if (!existing.getRut().equals(dto.getRut()) && repository.findByRut(dto.getRut()).isPresent()) {
            throw new DuplicateResourceException("Ya existe otro cliente con RUT: " + dto.getRut());
        }
        // Si cambia el email, validar que no esté ya usado por otro cliente
        if (!existing.getEmail().equalsIgnoreCase(dto.getEmail()) && repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Ya existe otro cliente con email: " + dto.getEmail());
        }
        // Si cambia el teléfono, validar que no esté ya usado por otro cliente
        if (!existing.getTelefono().equals(dto.getTelefono()) && repository.findByTelefono(dto.getTelefono()).isPresent()) {
            throw new DuplicateResourceException("Ya existe otro cliente con teléfono: " + dto.getTelefono());
        }

        mapper.updateEntityFromDto(dto, existing);
        // No se debe sobreescribir fechaRegistro ni activo si no vienen en el DTO, pero por seguridad:
        if (dto.getFechaRegistro() == null) existing.setFechaRegistro(existing.getFechaRegistro());
        if (dto.getActivo() == null) existing.setActivo(existing.getActivo());

        return mapper.toDto(repository.save(existing));
    }

    public ClienteDTO findById(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        return mapper.toDto(cliente);
    }

    public List<ClienteDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        cliente.setActivo(false);  // borrado lógico
        repository.save(cliente);
    }

    // ========== REPORTES ESPECIALIZADOS (5 reportes) ==========

    // Reporte 1: Clientes activos
    public List<ClienteDTO> findActivos() {
        return repository.findByActivoTrue().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    // Reporte 2: Buscar por RUT exacto (NUEVO)
    public ClienteDTO findByRut(String rut) {
        Cliente cliente = repository.findByRut(rut)
                .orElseThrow(() -> new ResourceNotFoundException("No existe cliente con RUT: " + rut));
        return mapper.toDto(cliente);
    }

    // Reporte 3: Buscar por email exacto
    public ClienteDTO findByEmail(String email) {
        Cliente cliente = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No existe cliente con email: " + email));
        return mapper.toDto(cliente);
    }

    // Reporte 4: Clientes cuyo nombre contenga una cadena
    public List<ClienteDTO> findByNombreContaining(String nombre) {
        return repository.findByNombreCompletoContainingIgnoreCase(nombre)
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    // Reporte 5: Clientes registrados en un rango de fechas (opcional, si lo deseas mantener)
    public List<ClienteDTO> findByFechaRegistroBetween(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null) inicio = LocalDateTime.MIN;
        if (fin == null) fin = LocalDateTime.now();
        return repository.findByFechaRegistroBetween(inicio, fin)
                .stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
