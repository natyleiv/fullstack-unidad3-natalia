package cl.vincalia.inventario_service.repository;

import cl.vincalia.inventario_service.model.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {
    Optional<Ingrediente> findByNombre(String nombre);
    List<Ingrediente> findByDisponibleTrue();
    List<Ingrediente> findByStockLessThan(Integer stock);
    List<Ingrediente> findByPrecioBetween(Double min, Double max);
}
