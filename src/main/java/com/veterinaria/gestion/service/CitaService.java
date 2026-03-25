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
import java.time.LocalTime;
import java.util.List;

//FALLLLTA BOTON DE AGREGAR CITA EN EL PANEL DE ADMIN
@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    public List<Cita> obtenerAgendaCompleta() {
        return citaRepository.findAllByOrderByFechaHoraAsc();
    }
    
    public List<Cita> obtenerMisCitas(String dni) {
        return citaRepository.findByMascotaClienteUsuarioUsernameOrderByFechaHoraAsc(dni);
    }
    
    @Transactional
    public void reservarCita(Cita cita) {
        // Lógica de negocio: Podrías validar que la fecha no sea anterior a "ahora"
        if (cita.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede programar una cita en el pasado.");
        }
        citaRepository.save(cita);
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
        // 1. Definimos el horario de la clínica (ej: de 09:00 a 14:00)
        List<String> horarioClinica = List.of("09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30");
        LocalDateTime ahora = LocalDateTime.now();

        // 2. Buscamos qué citas hay ese día en la BBDD
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.atTime(23, 59);
        List<Cita> citasDelDia = citaRepository.findByFechaHoraBetween(inicioDia, finDia);

        // 3. Extraemos solo las horas ocupadas en formato String "HH:mm"
        List<String> horasOcupadas = citasDelDia.stream()
                .map(c -> c.getFechaHora().toLocalTime().toString())
                .toList();

        // 4. Filtramos el horario de la clínica quitando las ocupadas
        return horarioClinica.stream()
                .filter(hora -> {
                    // Creamos un LocalDateTime con la fecha elegida y la hora del bucle
                    LocalDateTime fechaHoraCita = fecha.atTime(LocalTime.parse(hora));
                    
                    // REGLA: No debe estar ocupada Y debe ser después de "ahora"
                    return !horasOcupadas.contains(hora) && fechaHoraCita.isAfter(ahora);
                })
                .toList();
    }
}