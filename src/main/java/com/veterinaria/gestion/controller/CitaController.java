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

import jakarta.validation.Valid;
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
    public String verAgenda(Model model, Principal principal) {
        Authentication auth = (Authentication) principal;
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_admin"));
        String username = principal.getName();
        
        System.out.println("Roles del usuario: " + auth.getAuthorities());
        if (isAdmin) {
            // El ADMIN ve todo
            model.addAttribute("citas", citaService.obtenerAgendaCompleta());
            return "citas/agenda"; // Retorna el HTML de gestión
        } else {
        	// 1. Enviamos las citas del usuario
            model.addAttribute("citas", citaService.obtenerMisCitas(username));
            // El USUARIO solo ve las SUYAS
            model.addAttribute("mascotas", mascotaService.buscarMisMascotas(username));
            return "citas/mis-citas"; // Retorna el HTML de cliente
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
    @PostMapping("/reservar/guardar")
    @ResponseBody
    public ResponseEntity<?> guardarReserva(@Valid @ModelAttribute("cita") Cita cita, BindingResult result) {
        
        // 1. Si hay errores de validación (campos vacíos)
        if (result.hasErrors()) {
            String mensajeError = result.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body("{\"error\": \"" + mensajeError + "\"}");
        }

        try {
            citaService.reservarCita(cita);
            return ResponseEntity.ok().body("{\"mensaje\": \"¡Cita reservada con éxito!\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
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
