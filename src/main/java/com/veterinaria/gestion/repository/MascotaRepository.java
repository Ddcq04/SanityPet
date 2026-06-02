package com.veterinaria.gestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.veterinaria.gestion.model.Mascota;
import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
	
    // Obtiene todas las mascotas asociadas al ID de un cliente
    List<Mascota> findByClienteId(Long clienteId);
    
    // Buscador avanzado con filtros dinámicos que ignora mayúsculas, minúsculas y valores nulos
    @Query("SELECT m FROM Mascota m WHERE " +
           "(:nombre IS NULL OR LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:especie IS NULL OR LOWER(m.especie) LIKE LOWER(CONCAT('%', :especie, '%'))) AND " +
           "(:raza IS NULL OR LOWER(m.raza) LIKE LOWER(CONCAT('%', :raza, '%'))) AND " +
           "(:dueno IS NULL OR LOWER(m.cliente.usuario.nombreCompleto) LIKE LOWER(CONCAT('%', :dueno, '%')))")
    List<Mascota> buscarConFiltros(@Param("nombre") String nombre, 
                                   @Param("especie") String especie, 
                                   @Param("raza") String raza, 
                                   @Param("dueno") String dueno);
}