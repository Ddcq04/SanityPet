package com.veterinaria.gestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.veterinaria.gestion.model.Mascota;
import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
	
    // OBLIGATORIA: Para que el cliente vea solo sus mascotas
    List<Mascota> findByClienteId(Long clienteId);
    
 // EL BUSCADOR TOTAL: Filtra por todo a la vez si el campo no es nulo o vacío
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