package com.veterinaria.gestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.veterinaria.gestion.model.Mascota;
import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
	
    // OBLIGATORIA: Para que el cliente vea solo sus mascotas
    List<Mascota> findByClienteId(Long clienteId);

    // OPCIONAL: Filtros para el veterinario
    List<Mascota> findByEspecie(String especie);
    List<Mascota> findByRaza(String raza);
}