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

    @Autowired
    private com.veterinaria.gestion.service.PedidoService pedidoService;

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

    // --- MÉTODOS DEL CARRITO ---

    @SuppressWarnings("unchecked")
    private java.util.Map<Long, Integer> getCarrito(jakarta.servlet.http.HttpSession session) {
        java.util.Map<Long, Integer> carrito = (java.util.Map<Long, Integer>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new java.util.HashMap<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    @GetMapping("/carrito")
    public String verCarrito(jakarta.servlet.http.HttpSession session, Model model) {
        java.util.Map<Long, Integer> carrito = getCarrito(session);
        java.util.List<java.util.Map<String, Object>> items = new java.util.ArrayList<>();
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;

        for (java.util.Map.Entry<Long, Integer> entry : carrito.entrySet()) {
            Optional<Producto> productoOpt = productoService.obtenerPorId(entry.getKey());
            if (productoOpt.isPresent()) {
                Producto p = productoOpt.get();
                java.util.Map<String, Object> item = new java.util.HashMap<>();
                item.put("producto", p);
                item.put("cantidad", entry.getValue());
                java.math.BigDecimal subtotal = p.getPrecio().multiply(java.math.BigDecimal.valueOf(entry.getValue()));
                item.put("subtotal", subtotal);
                items.add(item);
                total = total.add(subtotal);
            }
        }

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        return "carrito";
    }

    @PostMapping("/carrito/agregar/{id}")
    public String agregarAlCarrito(@PathVariable("id") Long id, jakarta.servlet.http.HttpSession session, jakarta.servlet.http.HttpServletRequest request, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        Optional<Producto> productoOpt = productoService.obtenerPorId(id);
        if (productoOpt.isEmpty()) {
            return "redirect:/tienda";
        }

        Producto producto = productoOpt.get();
        java.util.Map<Long, Integer> carrito = getCarrito(session);
        int cantidadActual = carrito.getOrDefault(id, 0);

        if (cantidadActual + 1 > producto.getStock()) {
            redirectAttributes.addFlashAttribute("error", "No hay más stock disponible para " + producto.getNombre());
            String referer = request.getHeader("Referer");
            if (referer != null && referer.contains("/carrito")) {
                return "redirect:/tienda/carrito";
            }
            return "redirect:/tienda";
        }

        carrito.put(id, cantidadActual + 1);
        
        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("/carrito")) {
            return "redirect:/tienda/carrito";
        }
        return "redirect:/tienda";
    }

    @PostMapping("/carrito/quitar/{id}")
    public String quitarDelCarrito(@PathVariable("id") Long id, jakarta.servlet.http.HttpSession session) {
        java.util.Map<Long, Integer> carrito = getCarrito(session);
        if (carrito.containsKey(id)) {
            int nuevaCantidad = carrito.get(id) - 1;
            if (nuevaCantidad <= 0) {
                carrito.remove(id);
            } else {
                carrito.put(id, nuevaCantidad);
            }
        }
        return "redirect:/tienda/carrito";
    }

    @PostMapping("/carrito/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable("id") Long id, jakarta.servlet.http.HttpSession session) {
        java.util.Map<Long, Integer> carrito = getCarrito(session);
        carrito.remove(id);
        return "redirect:/tienda/carrito";
    }

    @PostMapping("/carrito/comprar")
    public String finalizarCompra(jakarta.servlet.http.HttpSession session, java.security.Principal principal, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        java.util.Map<Long, Integer> carrito = getCarrito(session);
        if (carrito.isEmpty()) {
            return "redirect:/tienda";
        }

        try {
            pedidoService.realizarPedido(principal.getName(), carrito);
            session.removeAttribute("carrito");
            redirectAttributes.addFlashAttribute("mensaje", "¡Compra realizada con éxito!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar la compra: " + e.getMessage());
            return "redirect:/tienda/carrito";
        }

        return "redirect:/home";
    }

    // --- API DINÁMICA ---

    @PostMapping("/api/carrito/agregar/{id}")
    @ResponseBody
    public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> agregarAlCarritoApi(@PathVariable("id") Long id, jakarta.servlet.http.HttpSession session) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        
        Optional<Producto> productoOpt = productoService.obtenerPorId(id);
        if (productoOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Producto no encontrado");
            return org.springframework.http.ResponseEntity.badRequest().body(response);
        }

        Producto producto = productoOpt.get();
        java.util.Map<Long, Integer> carrito = getCarrito(session);
        int cantidadActual = carrito.getOrDefault(id, 0);

        if (cantidadActual + 1 > producto.getStock()) {
            response.put("success", false);
            response.put("message", "No hay más stock disponible para " + producto.getNombre());
            return org.springframework.http.ResponseEntity.ok(response);
        }

        carrito.put(id, cantidadActual + 1);
        int totalItems = carrito.values().stream().mapToInt(Integer::intValue).sum();

        response.put("success", true);
        response.put("message", "¡" + producto.getNombre() + " añadido al carrito!");
        response.put("totalItems", totalItems);

        return org.springframework.http.ResponseEntity.ok(response);
    }
}
