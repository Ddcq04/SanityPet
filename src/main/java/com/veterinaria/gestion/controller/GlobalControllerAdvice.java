package com.veterinaria.gestion.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("cartCount")
    public int getCartCount(HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute("carrito");
        if (carrito == null) {
            return 0;
        }
        return carrito.values().stream().mapToInt(Integer::intValue).sum();
    }
}
