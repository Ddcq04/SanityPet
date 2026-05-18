package com.veterinaria.gestion.repository;

import com.veterinaria.gestion.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    java.util.List<Pedido> findByClienteIdOrderByFechaCompraDesc(Long clienteId);
}
