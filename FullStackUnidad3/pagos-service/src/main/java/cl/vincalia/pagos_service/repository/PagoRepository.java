package cl.vincalia.pagos_service.repository;

import cl.vincalia.pagos_service.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByPedidoId(Long pedidoId);
    List<Pago> findByEstado(String estado);
    List<Pago> findByMetodoPago(String metodoPago);
    List<Pago> findByFechaPagoBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Pago> findByMontoBetween(Double min, Double max);
}