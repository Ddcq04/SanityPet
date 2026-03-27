package com.veterinaria.gestion.controller;

import com.veterinaria.gestion.model.Mascota;
import com.veterinaria.gestion.service.MascotaService;
import com.veterinaria.gestion.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal; // Importante para la sesión

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

    // 2. NUEVO: El cliente ve sus propias mascotas
    // La URL será /mascotas/mis-mascotas
    @GetMapping("/mis-mascotas")
    public String verMisMascotas(Model model, Principal principal) {
        String dniLogueado = principal.getName(); // Obtiene el DNI del login
        model.addAttribute("mascotas", mascotaService.buscarMisMascotas(dniLogueado));
        return "mascotas/mis-mascotas"; // Thymeleaf: templates/mascotas/mis-mascotas.html
    }

 // 3. Formulario Nueva Mascota
    @GetMapping("/nueva")
    public String formularioNueva(Model model) {
        model.addAttribute("mascota", new Mascota());
        // IMPORTANTE: Enviamos la lista de clientes por si el ADMIN es quien crea la mascota
        model.addAttribute("todosClientes", clienteService.listarTodos());
        return "mascotas/formulario-mascota";
    }

    // 4. Editar Mascota
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        Mascota mascota = mascotaService.buscarPorId(id);
        if (mascota == null) return "redirect:/mascotas";
        
        model.addAttribute("mascota", mascota);
        model.addAttribute("todosClientes", clienteService.listarTodos());
        return "mascotas/formulario-mascota";
    }

    @PostMapping("/guardar")
    public String guardarMascota(@ModelAttribute("mascota") Mascota mascota, Principal principal, Authentication auth) {
        // Si la mascota viene sin cliente (porque la crea el propio usuario desde su perfil)
        if (mascota.getCliente() == null) {
            String dni = principal.getName();
            mascota.setCliente(clienteService.buscarPorDni(dni));
        }
        
        mascotaService.guardar(mascota);

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));

        return isAdmin ? "redirect:/mascotas" : "redirect:/mascotas/mis-mascotas";
    }
    
 // 6. Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id, Authentication auth) {
        mascotaService.eliminar(id);
        
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));

        return isAdmin ? "redirect:/mascotas" : "redirect:/mascotas/mis-mascotas";
    }
    
    @GetMapping("/buscar")
    public String buscarMascotas(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "especie", required = false) String especie,
            @RequestParam(value = "raza", required = false) String raza,
            Model model) {
        
        model.addAttribute("mascotas", mascotaService.buscarFiltrado(nombre, especie, raza));
        
        // Muy importante: devolvemos los valores al modelo para que el input no se vacíe al pulsar "Filtrar"
        model.addAttribute("nombreSeleccionado", nombre);
        model.addAttribute("especieSeleccionada", especie);
        model.addAttribute("razaSeleccionada", raza);
        
        return "mascotas/lista-mascota";
    }
}