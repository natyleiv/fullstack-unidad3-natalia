package cl.vincalia.cupones_service.repository;

import cl.vincalia.cupones_service.model.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CuponRepository extends JpaRepository<Cupon, Long> {
    Optional<Cupon> findByCodigo(String codigo);
    List<Cupon> findByActivoTrue();
    List<Cupon> findByUsadoFalse();
    List<Cupon> findByFechaVencimientoBefore(LocalDateTime fecha);
    List<Cupon> findByFechaVencimientoAfter(LocalDateTime fecha);
}
