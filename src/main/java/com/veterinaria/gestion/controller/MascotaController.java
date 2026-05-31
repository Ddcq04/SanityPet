package com.veterinaria.gestion.controller;

import com.veterinaria.gestion.model.Mascota;
import com.veterinaria.gestion.service.MascotaService;
import com.veterinaria.gestion.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;
    
    @Autowired
    private ClienteService clienteService;

    // 1. Listado general (Solo Admin/Vet)
    @GetMapping
    public String listarTodas(Model model) {
        model.addAttribute("mascotas", mascotaService.listarTodas());
        return "mascotas/lista-mascota";
    }

    // 2. El cliente ve sus propias mascotas (BLINDADO PARA ADMINS)
    @GetMapping("/mis-mascotas")
    public String verMisMascotas(Model model, Principal principal, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
        
        // Si es admin, lo redirigimos a la lista global para evitar el crash del DNI "admin"
        if (isAdmin) {
            return "redirect:/mascotas";
        }

        String dniLogueado = principal.getName();
        model.addAttribute("mascotas", mascotaService.buscarMisMascotas(dniLogueado));
        return "mascotas/mis-mascotas";
    }

    // 🌟 3. ABRIR FORMULARIO NUEVA MASCOTA (Faltaba este método)
    @GetMapping("/nueva")
    public String formularioNueva(Model model) {
        model.addAttribute("mascota", new Mascota());
        // Enviamos la lista de clientes para el buscador inteligente del ADMIN
        model.addAttribute("todosClientes", clienteService.listarTodos());
        return "mascotas/formulario-mascota";
    }

    // 🌟 4. ABRIR FORMULARIO EDITAR MASCOTA (Faltaba este método)
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model, Principal principal, Authentication auth) {
        String username = principal.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));

        try {
            Mascota mascota = mascotaService.buscarPorIdSeguro(id, username, isAdmin);
            if (mascota == null) return "redirect:/mascotas";
            
            model.addAttribute("mascota", mascota);
            model.addAttribute("todosClientes", clienteService.listarTodos());
            return "mascotas/formulario-mascota";
        } catch (RuntimeException e) {
            return isAdmin ? "redirect:/mascotas" : "redirect:/mascotas/mis-mascotas";
        }
    }

    // 5. Guardar Mascota (Mapeo Seguro y Redirección Absoluta)
    @PostMapping("/guardar")
    public String guardarMascota(@ModelAttribute("mascota") Mascota mascota, 
                                 Principal principal, 
                                 Authentication auth, 
                                 org.springframework.web.servlet.mvc.support.RedirectAttributes flash) {
        
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));

        // Detectar si estamos insertando una nueva o actualizando una existente
        boolean esEdicion = mascota.getId() != null;

        // Validar y asignar el propietario según el buscador o la sesión del cliente
        if (!isAdmin || mascota.getCliente() == null || mascota.getCliente().getId() == null) {
            String dni = principal.getName();
            mascota.setCliente(clienteService.buscarPorDni(dni));
        }
        
        mascotaService.guardar(mascota);

        // Añadir los mensajes flash que leerán los nuevos DIVS HTML
        if (esEdicion) {
            flash.addFlashAttribute("mensajeExito", "¡Mascota modificada correctamente!");
        } else {
            flash.addFlashAttribute("mensajeExito", "¡Mascota agregada correctamente!");
        }

        return isAdmin ? "redirect:/mascotas" : "redirect:/mascotas/mis-mascotas";
    }
    
    // 6. Eliminar Mascota con Alerta
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id, 
                           Authentication auth, 
                           org.springframework.web.servlet.mvc.support.RedirectAttributes flash) {
        try {
            mascotaService.eliminar(id);
            flash.addFlashAttribute("mensajeExito", "¡Mascota eliminada correctamente!");
        } catch (RuntimeException e) {
            flash.addFlashAttribute("mensajeError", "No se pudo eliminar la mascota.");
        }
        
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));

        return isAdmin ? "redirect:/mascotas" : "redirect:/mascotas/mis-mascotas";
    }
    
    // 7. Filtro de búsqueda
    @GetMapping("/buscar")
    public String buscarMascotas(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "especie", required = false) String especie,
            @RequestParam(value = "raza", required = false) String raza,
            @RequestParam(value = "dueno", required = false) String dueno,
            Model model) {
        
        model.addAttribute("mascotas", mascotaService.buscarFiltrado(nombre, especie, raza, dueno));
        
        model.addAttribute("nombreSeleccionado", nombre);
        model.addAttribute("especieSeleccionada", especie);
        model.addAttribute("razaSeleccionada", raza);
        model.addAttribute("duenoSeleccionado", dueno);
        
        return "mascotas/lista-mascota";
    }
}