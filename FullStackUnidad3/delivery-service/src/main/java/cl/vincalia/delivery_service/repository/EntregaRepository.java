package cl.vincalia.delivery_service.repository;

import cl.vincalia.delivery_service.model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {
    List<Entrega> findByPedidoId(Long pedidoId);
    List<Entrega> findByEstado(String estado);
    List<Entrega> findByRepartidor(String repartidor);
    List<Entrega> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Entrega> findByFechaEstimadaBetween(LocalDateTime inicio, LocalDateTime fin);
}