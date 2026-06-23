package cl.vincalia.proveedores_service.repository;

import cl.vincalia.proveedores_service.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findByRut(String rut);
    Optional<Proveedor> findByEmailContacto(String email);
    List<Proveedor> findByRubro(String rubro);
    List<Proveedor> findByActivoTrue();
    List<Proveedor> findByRazonSocialContainingIgnoreCase(String nombre);
}