package com.veterinaria.gestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.veterinaria.gestion.model.Cliente;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Busca un cliente por su DNI
    Optional<Cliente> findByDni(String dni);

    // Filtra clientes por coincidencia parcial en el nombre completo (ignora mayúsculas/minúsculas)
    List<Cliente> findByUsuarioNombreCompletoContainingIgnoreCase(String nombre);

    // Busca un cliente por el nombre de usuario de su cuenta asociada
    Optional<Cliente> findByUsuarioUsername(String username);
}