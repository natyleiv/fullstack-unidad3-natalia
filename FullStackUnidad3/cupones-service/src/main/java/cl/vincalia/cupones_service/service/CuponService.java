package cl.vincalia.cupones_service.service;

import cl.vincalia.cupones_service.dto.CuponDTO;
import cl.vincalia.cupones_service.dto.CuponRequestDTO;
import cl.vincalia.cupones_service.exception.DuplicateResourceException;
import cl.vincalia.cupones_service.exception.ResourceNotFoundException;
import cl.vincalia.cupones_service.mapper.CuponMapper;
import cl.vincalia.cupones_service.model.Cupon;
import cl.vincalia.cupones_service.repository.CuponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuponService {

    private final CuponRepository repository;
    private final CuponMapper mapper;

    @Transactional
    public CuponDTO crear(CuponRequestDTO request) {
        if (repository.findByCodigo(request.getCodigo()).isPresent()) {
            throw new DuplicateResourceException("Ya existe un cupón con código: " + request.getCodigo());
        }
        Cupon cupon = mapper.toEntity(request);
        return mapper.toDto(repository.save(cupon));
    }

    public CuponDTO obtenerPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado con id: " + id));
    }

    public CuponDTO obtenerPorCodigo(String codigo) {
        return repository.findByCodigo(codigo)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado con código: " + codigo));
    }

    public List<CuponDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public CuponDTO actualizar(Long id, CuponRequestDTO request) {
        Cupon existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado"));

        if (!existing.getCodigo().equalsIgnoreCase(request.getCodigo()) &&
                repository.findByCodigo(request.getCodigo()).isPresent()) {
            throw new DuplicateResourceException("Ya existe otro cupón con código: " + request.getCodigo());
        }

        mapper.updateEntity(request, existing);
        return mapper.toDto(repository.save(existing));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cupón no encontrado");
        }
        repository.deleteById(id);
    }

    @Transactional
    public void marcarUsado(String codigo) {
        Cupon cupon = repository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado"));
        if (cupon.getUsado()) {
            throw new RuntimeException("El cupón ya fue usado");
        }
        if (cupon.getFechaVencimiento().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El cupón está vencido");
        }
        cupon.setUsado(true);
        repository.save(cupon);
    }

    // ========== REPORTES ==========
    public List<CuponDTO> findActivos() {
        return repository.findByActivoTrue().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<CuponDTO> findNoUsados() {
        return repository.findByUsadoFalse().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<CuponDTO> findVencidos() {
        return repository.findByFechaVencimientoBefore(LocalDateTime.now()).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<CuponDTO> findVigentes() {
        return repository.findByFechaVencimientoAfter(LocalDateTime.now()).stream().map(mapper::toDto).collect(Collectors.toList());
    }
}