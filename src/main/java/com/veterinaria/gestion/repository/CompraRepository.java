package com.veterinaria.gestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.veterinaria.gestion.model.Compra;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
	
	// Obtiene el historial de compras de un cliente ordenado desde la más reciente
    List<Compra> findByClienteIdOrderByFechaCompraDesc(Long clienteId);
}
