package com.veterinaria.gestion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import com.veterinaria.gestion.service.ClienteService;
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
 // --- FLUJO DE REGISTRO PÚBLICO ---

    @GetMapping("/registrarse")
    public String mostrarFormularioRegistro(Model model) {
        com.veterinaria.gestion.model.Cliente cliente = new com.veterinaria.gestion.model.Cliente();
        cliente.setUsuario(new com.veterinaria.gestion.model.Usuario());
        model.addAttribute("cliente", cliente);
        return "registrarse"; // Tu nuevo HTML de alta de socio
    }

    @PostMapping("/registrar-socio")
    public String registrarSocio(@Valid @ModelAttribute("cliente") com.veterinaria.gestion.model.Cliente cliente, 
                                 BindingResult result, Model model) {
        
        // 1. Validar que las contraseñas coincidan
        if (!cliente.getUsuario().getPasswordPlana().equals(cliente.getUsuario().getPasswordRepeat())) {
            result.rejectValue("usuario.passwordRepeat", "error.usuario", "Las contraseñas no coinciden");
        }

        // 2. Si hay errores (incluyendo los de @NotBlank), volver a la vista
        if (result.hasErrors()) {
            return "registrarse";
        }

        try {
            // 3. Sincronizar datos Cliente -> Usuario
            // En tu BBDD, nombre_completo es obligatorio. Usaremos el DNI o un campo de nombre si lo añades.
            cliente.getUsuario().setUsername(cliente.getDni());
            cliente.getUsuario().setRol("user");
            
            // 4. Encriptar (NoOp o BCrypt según tu SecurityConfig)
            cliente.getUsuario().setPassword(passwordEncoder.encode(cliente.getUsuario().getPasswordPlana()));

            // 5. Guardar (Asegúrate de que Cliente.java tenga cascade = CascadeType.ALL)
            clienteService.guardar(cliente);
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar: " + e.getMessage());
            return "registrarse";
        }

        return "redirect:/login?registrado=true";
    }
}