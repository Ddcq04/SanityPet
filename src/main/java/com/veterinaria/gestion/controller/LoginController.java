package com.veterinaria.gestion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Busca templates/login.html
    }
    
    @GetMapping("/")
    public String index() {
        return "redirect:/login"; // Si entran a la raíz, los manda al login
    }
    
    @GetMapping("/home")
    public String home() {
        return "home"; // Esto buscará src/main/resources/templates/home.html
    }
}