package com.veterinaria.gestion.controller;

import com.veterinaria.gestion.model.Cliente;
import com.veterinaria.gestion.model.Usuario;
import com.veterinaria.gestion.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login"; 
    }
    
    @GetMapping("/")
    public String index() {
        return "redirect:/home"; 
    }
    
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/politicas/privacidad")
    public String politicaPrivacidad() {
        return "politicas/politica";
    }

    @GetMapping("/politicas/cookies")
    public String politicaCookies() {
        return "politicas/cookies";
    }

    @GetMapping("/politicas/aviso-legal")
    public String avisoLegal() {
        return "politicas/aviso";
    }

    // --- FLUJO DE REGISTRO PÚBLICO (VISITANTE) ---

    @GetMapping("/registrarse")
    public String mostrarFormularioRegistro(Model model) {
        Cliente cliente = new Cliente();
        Usuario usuario = new Usuario();
        cliente.setUsuario(usuario);
        
        model.addAttribute("cliente", cliente);
        model.addAttribute("fromAdmin", false); // 🌟 Obligamos a false porque entra un visitante libre
        return "registrarse";
    }

    @PostMapping("/registrar-socio")
    public String registrarSocio(@Valid @ModelAttribute("cliente") Cliente cliente, 
                                 BindingResult result, 
                                 Model model) {
        
        Usuario usuario = cliente.getUsuario();

        // 1. Validaciones manuales de contraseñas
        if (usuario == null || usuario.getPasswordPlana() == null || usuario.getPasswordPlana().trim().isEmpty()) {
            result.rejectValue("usuario.passwordPlana", "error.usuario", "La contraseña es obligatoria");
        }
        
        if (usuario != null && usuario.getPasswordPlana() != null && 
            !usuario.getPasswordPlana().equals(usuario.getPasswordRepeat())) {
            result.rejectValue("usuario.passwordRepeat", "error.usuario", "Las contraseñas no coinciden");
        }

        // 2. Si hay errores iniciales de contraseñas o campos vacíos, volvemos atrás
        if (result.hasErrors()) {
            model.addAttribute("fromAdmin", false);
            return "registrarse";
        }

        try {
            // 3. Preparamos el objeto para guardar
            usuario.setUsername(cliente.getDni());
            usuario.setRol("user");
            usuario.setPassword(passwordEncoder.encode(usuario.getPasswordPlana()));

            // 4. Guardado definitivo (Al usar saveAndFlush en el servicio, si el DNI/username existe, explotará AQUÍ mismo)
            clienteService.guardar(cliente);
            
            return "redirect:/login?registrado=true"; 
            
        } catch (Exception e) {
            // 🌟 5. EL CATCH AHORA SÍ ATRAPA EL DNI DUPLICADO DEL ADMIN
            System.out.println("Error detectado en el registro: " + e.getMessage());
            
            // Metemos el error directamente en el campo DNI para que Thymeleaf lo pinte en rojo
            result.rejectValue("dni", "error.cliente", "Lo sentimos, este DNI ya está registrado en el sistema.");
            
            model.addAttribute("fromAdmin", false);
            return "registrarse"; // Evitamos el redirect y lo dejamos en la pantalla con el mensaje
        }
    }
}