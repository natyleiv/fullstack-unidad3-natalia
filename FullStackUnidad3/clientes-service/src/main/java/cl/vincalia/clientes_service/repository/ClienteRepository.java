package cl.vincalia.clientes_service.repository;

import cl.vincalia.clientes_service.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByRut(String rut);

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByTelefono(String telefono);

    List<Cliente> findByActivoTrue();

    List<Cliente> findByNombreCompletoContainingIgnoreCase(String nombre);

    // ✅ Método para reporte por rango de fechas
    List<Cliente> findByFechaRegistroBetween(LocalDateTime inicio, LocalDateTime fin);
}