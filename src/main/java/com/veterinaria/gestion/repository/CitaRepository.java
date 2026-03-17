package com.veterinaria.gestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.veterinaria.gestion.model.Cita;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
	
    // OBLIGATORIA: Agenda ordenada cronológicamente
    List<Cita> findAllByOrderByFechaHoraAsc();

    // OPCIONAL: Resumen diario (Citas entre dos horas, ej: hoy a las 00:00 y hoy a las 23:59)
    List<Cita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    // OPCIONAL: Historial médico (Cuentas citas tiene una mascota)
    long countByMascotaId(Long mascotaId);
}