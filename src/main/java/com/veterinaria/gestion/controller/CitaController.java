package com.veterinaria.gestion.controller;

import com.veterinaria.gestion.model.Mascota;
import com.veterinaria.gestion.model.Cita;
import com.veterinaria.gestion.service.CitaService;
import com.veterinaria.gestion.service.MascotaService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private MascotaService mascotaService;

    // 1. Mostrar la agenda según el rol de quien accede (Admin o Cliente)
    @GetMapping
    public String verAgenda(@RequestParam(value = "filtro", required = false) String filtro, 
            @RequestParam(value = "mascotaId", required = false) Long mascotaId, 
            Model model, Principal principal) {
        
        Authentication auth = (Authentication) principal;
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_admin"));
        String username = principal.getName();
        
        if (isAdmin) {
            List<Cita> listaCitas;
            String titulo;

            if (mascotaId != null) {
                listaCitas = citaService.obtenerHistorialMascota(mascotaId); 
                Mascota m = mascotaService.buscarPorId(mascotaId);
                titulo = "Historial de citas: " + m.getNombre();
            } else if ("hoy".equals(filtro)) {
                listaCitas = citaService.obtenerCitasHoy();
                titulo = "Citas para Hoy";
            } else if ("semana".equals(filtro)) {
                listaCitas = citaService.obtenerCitasSemana();
                titulo = "Citas de los próximos 7 días";
            } else {
                listaCitas = citaService.obtenerAgendaCompleta();
                titulo = "Agenda Completa";
            }

            model.addAttribute("citas", listaCitas);
            model.addAttribute("tituloFiltro", titulo);
            model.addAttribute("filtroActivo", filtro); 
            model.addAttribute("todasLasMascotas", mascotaService.listarTodas());
            model.addAttribute("mascotaSeleccionada", mascotaId);
            
            return "citas/agenda"; 
        } else {
            model.addAttribute("citas", citaService.obtenerMisCitas(username));
            model.addAttribute("mascotas", mascotaService.buscarMisMascotas(username));
            return "citas/mis-citas";
        }
    }
    
    // 2. Endpoint REST para devolver las horas disponibles asíncronamente (JS Fetch)
    @GetMapping("/horas-disponibles")
    @ResponseBody
    public List<String> getHoras(@RequestParam("fecha") String fechaStr) {
        LocalDate fecha = LocalDate.parse(fechaStr);
        return citaService.obtenerHorasLibres(fecha);
    }
    
    // 3. Mostrar formulario de Nueva Cita
    @GetMapping("/reservar")
    public String formularioReservaRapida(Model model, Principal principal) {
        Authentication auth = (Authentication) principal;
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
        
        model.addAttribute("cita", new Cita());
        model.addAttribute("esEdicion", false);

        if (isAdmin) {
            model.addAttribute("listaMascotas", mascotaService.listarTodas());
        } else {
            String Dni = principal.getName();
            model.addAttribute("listaMascotas", mascotaService.buscarMisMascotas(Dni));
        }
        
        return "citas/reserva-rapida";
    }

    // 4. Guardar o modificar cita (BLINDADO FRENTE A CAMPOS DISABLED)
 // 4. Guardar o modificar cita
    @PostMapping("/reservar/guardar")
    @ResponseBody
    public ResponseEntity<?> guardarReserva(@ModelAttribute("cita") Cita cita, BindingResult result) {
        
        if (result.hasErrors()) {
            boolean soloErrorFecha = result.getFieldErrors().stream()
                    .allMatch(error -> error.getField().equals("fechaHora"));

            if (!soloErrorFecha || (cita.getId() == null && cita.getFechaHora() == null)) {
                String mensajeError = result.getFieldError().getDefaultMessage();
                return ResponseEntity.badRequest().body("{\"error\": \"" + mensajeError + "\"}");
            }
        }

        try {
            // Si es una edición...
            if (cita.getId() != null) {
                Cita citaExistente = citaService.buscarPorId(cita.getId());
                if (citaExistente != null) {
                    
                    // Comprobamos si la cita original pertenece al pasado
                    java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
                    if (citaExistente.getFechaHora().isBefore(ahora)) {
                        // SI YA PASÓ: Blindamos los campos inmutables para que no cambien
                        cita.setMascota(citaExistente.getMascota());
                        cita.setMotivo(citaExistente.getMotivo());
                        cita.setFechaHora(citaExistente.getFechaHora()); // Mantiene su fecha histórica
                    }
                    // SI ES FUTURA: No hacemos nada aquí, dejamos que los nuevos valores del formulario entren directamente.
                }
            }

            citaService.reservarCita(cita); 
            return ResponseEntity.ok().body("{\"mensaje\": \"¡Cita guardada con éxito!\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    // 5. Mostrar formulario para Editar una Cita Existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model, Principal principal) {
        Cita cita = citaService.buscarPorId(id);
        Authentication auth = (Authentication) principal;
        String username = principal.getName();
        
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));

        // SEGURIDAD: Si no es administrador, verificar estrictamente que la cita le pertenezca
        if (!isAdmin && !cita.getMascota().getCliente().getUsuario().getUsername().equals(username)) {
            return "redirect:/citas?error=access-denied";
        }

        if (isAdmin) {
            model.addAttribute("listaMascotas", mascotaService.listarTodas());
        } else {
            model.addAttribute("listaMascotas", mascotaService.buscarMisMascotas(username));
        }

        model.addAttribute("cita", cita);
        model.addAttribute("esEdicion", true); // Esto activará los bloques "th:disabled" en el HTML
        return "citas/reserva-rapida";
    }

    // 6. Cancelar una cita
    @GetMapping("/cancelar/{id}")
    public String cancelar(@PathVariable("id") Long id, Principal principal, RedirectAttributes flash) {
        try {
            citaService.cancelarCita(id, principal);
            flash.addFlashAttribute("mensajeExito", "Cita cancelada correctamente.");
        } catch (RuntimeException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/citas";
    }
}