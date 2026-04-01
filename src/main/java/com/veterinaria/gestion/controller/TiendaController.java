package com.veterinaria.gestion.controller;

import com.veterinaria.gestion.model.Producto;
import com.veterinaria.gestion.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/tienda")
public class TiendaController {

    @Autowired
    private ProductoService productoService;

    // Listar todos los productos
    @GetMapping
    public String listarProductos(Model model, jakarta.servlet.http.HttpServletRequest request) {
        model.addAttribute("productos", productoService.obtenerTodos());
        model.addAttribute("isAdmin", request.isUserInRole("admin") || request.isUserInRole("ROLE_admin"));
        return "tienda";
    }


    @GetMapping("/nuevo")
    public String mostrarFormularioDeCreacion(Model model) {
        model.addAttribute("producto", new Producto());
        return "formulario-producto";
    }


    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable("id") Long id, Model model) {
        Optional<Producto> productoOpt = productoService.obtenerPorId(id);
        if (productoOpt.isPresent()) {
            model.addAttribute("producto", productoOpt.get());
            return "formulario-producto";
        }
        return "redirect:/tienda";
    }


    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        productoService.guardar(producto);
        return "redirect:/tienda";
    }


    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") Long id) {
        productoService.eliminar(id);
        return "redirect:/tienda";
    }
}
