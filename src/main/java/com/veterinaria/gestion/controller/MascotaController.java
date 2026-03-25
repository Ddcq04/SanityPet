package com.veterinaria.gestion.controller;

import com.veterinaria.gestion.model.Mascota;
import com.veterinaria.gestion.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal; // Importante para la sesión

@Controller
@RequestMapping("/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    // 1. Listado general (Solo Admin/Vet)
    @GetMapping
    public String listarTodas(Model model) {
        model.addAttribute("mascotas", mascotaService.listarTodas());
        return "mascotas/lista";
    }

    // 2. NUEVO: El cliente ve sus propias mascotas
    // La URL será /mascotas/mis-mascotas
    @GetMapping("/mis-mascotas")
    public String verMisMascotas(Model model, Principal principal) {
        String dniLogueado = principal.getName(); // Obtiene el DNI del login
        model.addAttribute("mascotas", mascotaService.buscarMisMascotas(dniLogueado));
        return "mascotas/mis-mascotas"; // Thymeleaf: templates/mascotas/mis-mascotas.html
    }

    // 3. Ver mascotas de un dueño específico (Para el botón del Admin/Vet)
    @GetMapping("/dueño/{id}")
    public String listarPorDueño(@PathVariable("id") Long clienteId, Model model) {
        model.addAttribute("mascotas", mascotaService.buscarPorDueño(clienteId));
        return "mascotas/lista-cliente"; 
    }

    // 4. Formulario para añadir mascota
    @GetMapping("/nueva")
    public String formularioNueva(Model model) {
        model.addAttribute("mascota", new Mascota());
        return "mascotas/formulario-mascota";
    }

    @PostMapping("/guardar")
    public String guardarMascota(@ModelAttribute("mascota") Mascota mascota) {
        mascotaService.guardar(mascota);
        return "redirect:/mascotas";
    }
}