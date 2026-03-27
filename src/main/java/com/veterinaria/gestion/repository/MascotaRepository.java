package com.veterinaria.gestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.veterinaria.gestion.model.Mascota;
import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
	
    // OBLIGATORIA: Para que el cliente vea solo sus mascotas
    List<Mascota> findByClienteId(Long clienteId);
    
    List<Mascota> findByNombreContainingIgnoreCase(String nombre);
    // OPCIONAL: Filtros para el veterinario
    List<Mascota> findByEspecie(String especie);
    List<Mascota> findByRaza(String raza);

	List<Mascota> findByEspecieAndRaza(String especie, String raza);
	// Filtra por nombre Y especie a la vez
    List<Mascota> findByNombreContainingIgnoreCaseAndEspecieContainingIgnoreCase(String nombre, String especie);

    // Si también quieres añadir la raza al mix:
    List<Mascota> findByNombreContainingIgnoreCaseAndEspecieContainingIgnoreCaseAndRazaContainingIgnoreCase(String nombre, String especie, String raza);
}