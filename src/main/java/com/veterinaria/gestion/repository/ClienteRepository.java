package com.veterinaria.gestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.veterinaria.gestion.model.Cliente;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
    // OBLIGATORIA: Para el perfil del dueño
    Optional<Cliente> findByDni(String dni);

    // OPCIONAL: Para avisos de saldo bajo
    List<Cliente> findBySaldoLessThan(BigDecimal limite);
}