package com.veterinaria.gestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.veterinaria.gestion.model.Cliente;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	Optional<Cliente> findByDni(String dni);
	List<Cliente> findByUsuarioNombreCompletoContainingIgnoreCase(String nombre);
}