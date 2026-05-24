package com.veterinaria.gestion.service;

import com.veterinaria.gestion.model.Cita;
import com.veterinaria.gestion.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Collections;

//FALLLLTA BOTON DE AGREGAR CITA EN EL PANEL DE ADMIN
@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    public List<Cita> obtenerAgendaCompleta() {
        return citaRepository.findAllByOrderByFechaHoraAsc();
    }
    
    public List<Cita> obtenerHistorialMascota(Long mascotaId) {
        return citaRepository.findByMascotaIdOrderByFechaHoraDesc(mascotaId);
    }
    
    public List<Cita> obtenerMisCitas(String dni) {
        return citaRepository.findByMascotaClienteUsuarioUsernameOrderByFechaHoraAsc(dni);
    }
    
    public Cita buscarPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La cita no existe."));
    }
    
    // --- FILTRO: CITAS DE HOY (Ordenadas) ---
    public List<Cita> obtenerCitasHoy() {
        LocalDateTime inicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fin = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        
        return citaRepository.findByFechaHoraBetweenOrderByFechaHoraAsc(inicio, fin);
    }

    // --- FILTRO: CITAS DE LA SEMANA (Ordenadas) ---
    public List<Cita> obtenerCitasSemana() {
        LocalDateTime inicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        // Próximos 7 días naturales
        LocalDateTime fin = inicio.plusDays(7).withHour(23).withMinute(59).withSecond(59);
        
        return citaRepository.findByFechaHoraBetweenOrderByFechaHoraAsc(inicio, fin);
    }
    
    @Transactional
    public void reservarCita(Cita cita) {
        Cita citaFinal;

        if (cita.getId() != null) {
            // === MODO EDICIÓN SEGURO ===
            // Recuperamos la entidad directamente dentro de esta transacción (Estado Managed)
            Cita citaOriginal = citaRepository.findById(cita.getId())
                    .orElseThrow(() -> new RuntimeException("La cita no existe."));

            // Actualizamos los campos editables permitidos
            citaOriginal.setMascota(cita.getMascota());
            citaOriginal.setMotivo(cita.getMotivo());
            citaOriginal.setDescripcion(cita.getDescripcion()); // Guarda tus observaciones clínicas

            // Si el front envió una nueva fecha/hora (cita futura), la actualizamos
            if (cita.getFechaHora() != null) {
                // Validación de fin de semana para la nueva fecha propuesta
                DayOfWeek diaCita = cita.getFechaHora().getDayOfWeek();
                if (diaCita == DayOfWeek.SATURDAY || diaCita == DayOfWeek.SUNDAY) {
                    throw new RuntimeException("La clínica solo atiende de lunes a viernes.");
                }
                citaOriginal.setFechaHora(cita.getFechaHora());
            }
            // Si el front la mandó nula (porque estaba deshabilitada por ser cita pasada), 
            // 'citaOriginal' conserva intacta su fecha histórica original.

            citaFinal = citaOriginal;

        } else {
            // === MODO NUEVA CITA ===
            if (cita.getFechaHora() == null) {
                throw new RuntimeException("La fecha y hora son obligatorias.");
            }

            // Validar que no sea en el pasado
            if (cita.getFechaHora().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("No se puede programar una cita en el pasado.");
            }
            
            // Validar fin de semana
            DayOfWeek diaCita = cita.getFechaHora().getDayOfWeek();
            if (diaCita == DayOfWeek.SATURDAY || diaCita == DayOfWeek.SUNDAY) {
                throw new RuntimeException("La clínica solo atiende de lunes a viernes.");
            }

            citaFinal = cita;
        }
        
        // Guardado limpio sin conflictos de estados de Hibernate
        citaRepository.save(citaFinal);
    }

    @Transactional
    public void cancelarCita(Long citaId, Principal principal) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("La cita no existe."));

        // Convertimos principal a Authentication para ver los roles
        Authentication auth = (Authentication) principal;
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_admin"));
        String usernameLogueado = principal.getName();

        // Lógica: Si NO es admin Y el dueño no es el logueado -> ERROR
        String dniDueno = cita.getMascota().getCliente().getUsuario().getUsername();
        
        if (!isAdmin && !dniDueno.equals(usernameLogueado)) {
            throw new RuntimeException("No tienes permiso para cancelar esta cita.");
        }

        citaRepository.delete(cita);
    }

    public List<String> obtenerHorasLibres(LocalDate fecha) {
        // --- NUEVA VALIDACIÓN: Si es fin de semana, devolvemos una lista vacía de inmediato ---
        DayOfWeek dia = fecha.getDayOfWeek();
        if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) {
            return Collections.emptyList(); // No devuelve horas disponibles
        }

        // 1. Definimos el horario de la clínica (de lunes a viernes)
        List<String> horarioClinica = List.of("09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30");
        LocalDateTime ahora = LocalDateTime.now();

        // 2. Buscamos qué citas hay ese día en la BBDD
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.atTime(23, 59);
        List<Cita> citasDelDia = citaRepository.findByFechaHoraBetweenOrderByFechaHoraAsc(inicioDia, finDia);

        // 3. Extraemos solo las horas ocupadas en formato String "HH:mm"
        List<String> horasOcupadas = citasDelDia.stream()
                .map(c -> c.getFechaHora().toLocalTime().toString())
                .toList();

        // 4. Filtramos el horario de la clínica quitando las ocupadas
        return horarioClinica.stream()
                .filter(hora -> {
                    LocalDateTime fechaHoraCita = fecha.atTime(LocalTime.parse(hora));
                    return !horasOcupadas.contains(hora) && fechaHoraCita.isAfter(ahora);
                })
                .toList();
    }
}