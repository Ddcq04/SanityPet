package com.veterinaria.gestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.veterinaria.gestion.model.Cita;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
	
    // OBLIGATORIA: Agenda ordenada cronológicamente
	@Query("SELECT c FROM Cita c " +
	           "LEFT JOIN FETCH c.mascota m " +
	           "LEFT JOIN FETCH m.cliente cl " +
	           "LEFT JOIN FETCH cl.usuario u " +
	           "ORDER BY c.fechaHora ASC")
	List<Cita> findAllByOrderByFechaHoraAsc();
    List<Cita> findByMascotaClienteUsuarioUsernameOrderByFechaHoraAsc(String username);

    // OPCIONAL: Resumen diario (Citas entre dos horas, ej: hoy a las 00:00 y hoy a las 23:59)
    List<Cita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
}