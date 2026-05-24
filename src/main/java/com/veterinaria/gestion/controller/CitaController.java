package com.veterinaria.gestion.controller;

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

    // 1. Mostrar una vista u otra segun el rol de quien accede
    @GetMapping
    public String verAgenda(@RequestParam(value = "filtro", required = false) String filtro, 
                           Model model, Principal principal) {
        
        Authentication auth = (Authentication) principal;
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_admin"));
        String username = principal.getName();
        
        if (isAdmin) {
            // LÓGICA PARA ADMIN (VETERINARIO) CON FILTROS
            List<Cita> listaCitas;
            String titulo;

            if ("hoy".equals(filtro)) {
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
            model.addAttribute("filtroActivo", filtro); // Para resaltar el botón en el HTML
            
            return "citas/agenda"; 
        } else {
            // LÓGICA PARA CLIENTE (Sin cambios)
            model.addAttribute("citas", citaService.obtenerMisCitas(username));
            model.addAttribute("mascotas", mascotaService.buscarMisMascotas(username));
            return "citas/mis-citas";
        }
    }
    
    // Muestra de horas disponibles para el front
    @GetMapping("/horas-disponibles")
    @ResponseBody
    public List<String> getHoras(@RequestParam("fecha") String fechaStr) {
        LocalDate fecha = LocalDate.parse(fechaStr);
        return citaService.obtenerHorasLibres(fecha);
    }
    
    // 3. Reservar cita
    @GetMapping("/reservar")
    public String formularioReservaRapida(Model model, Principal principal) {
        Authentication auth = (Authentication) principal;
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
        
        model.addAttribute("cita", new Cita());
        model.addAttribute("esEdicion", false);

        if (isAdmin) {
            // Si es Admin, cargamos TODAS las mascotas de la clínica
            model.addAttribute("listaMascotas", mascotaService.listarTodas());
        } else {
            // Si es Cliente, solo las suyas
            String dni = principal.getName();
            model.addAttribute("listaMascotas", mascotaService.buscarMisMascotas(dni));
        }
        
        return "citas/reserva-rapida";
    }
    //4. Guardar cita
  //4. Guardar cita
  //4. Guardar cita
    @PostMapping("/reservar/guardar")
    @ResponseBody
    public ResponseEntity<?> guardarReserva(@ModelAttribute("cita") Cita cita, BindingResult result) {
        
        // Evaluamos errores de validación (ignorando la fecha si es una edición pasada donde el front la mandó nula)
        if (result.hasErrors()) {
            boolean soloErrorFecha = result.getFieldErrors().stream()
                    .allMatch(error -> error.getField().equals("fechaHora"));

            // Si hay otros errores diferentes a la fecha, o si es cita nueva y falta la fecha:
            if (!soloErrorFecha || (cita.getId() == null && cita.getFechaHora() == null)) {
                String mensajeError = result.getFieldError().getDefaultMessage();
                return ResponseEntity.badRequest().body("{\"error\": \"" + mensajeError + "\"}");
            }
        }

        try {
            // Pasamos el objeto directamente al servicio; él se encargará de fusionarlo de forma segura
            citaService.reservarCita(cita); 
            return ResponseEntity.ok().body("{\"mensaje\": \"¡Cita guardada con éxito!\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model, Principal principal) {
        Cita cita = citaService.buscarPorId(id);
        Authentication auth = (Authentication) principal;
        String username = principal.getName();
        
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));

        // SEGURIDAD: Si no es admin, verificar que la cita es suya
        if (!isAdmin && !cita.getMascota().getCliente().getUsuario().getUsername().equals(username)) {
            return "redirect:/citas?error=access-denied";
        }

        if (isAdmin) {
            model.addAttribute("listaMascotas", mascotaService.listarTodas());
        } else {
            model.addAttribute("listaMascotas", mascotaService.buscarMisMascotas(username));
        }

        model.addAttribute("cita", cita);
        model.addAttribute("esEdicion", true); 
        return "citas/reserva-rapida";
    }

    // 5. Cancelar cita
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
