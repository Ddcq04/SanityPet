package com.veterinaria.gestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.veterinaria.gestion.model.Compra;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
	
    // OPCIONAL: Historial de compras de un cliente concreto
    List<Compra> findByClienteIdOrderByFechaCompraDesc(Long clienteId);
}
