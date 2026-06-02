package com.veterinaria.gestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.veterinaria.gestion.model.Cita;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

	//Devuelve todas las citas ordenadas por fecha de forma ascendente
	@Query("SELECT c FROM Cita c " +
	           "LEFT JOIN FETCH c.mascota m " +
	           "LEFT JOIN FETCH m.cliente cl " +
	           "LEFT JOIN FETCH cl.usuario u " +
	           "ORDER BY c.fechaHora ASC")
	List<Cita> findAllByOrderByFechaHoraAsc();
	
	//Obtiene las citas de un cliente filtrando por su nombre de usuario
    List<Cita> findByMascotaClienteUsuarioUsernameOrderByFechaHoraAsc(String username);
    
    //Recupera las citas comprendidas dentro de un rango de fechas determinado
    List<Cita> findByFechaHoraBetweenOrderByFechaHoraAsc(LocalDateTime inicio, LocalDateTime fin);
    
    //Devuelve el historial de citas de una mascota específica, de la más reciente a la más antigua
    List<Cita> findByMascotaIdOrderByFechaHoraDesc(Long mascotaId);
}

