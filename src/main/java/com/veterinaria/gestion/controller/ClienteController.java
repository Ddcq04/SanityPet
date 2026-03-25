package com.veterinaria.gestion.controller;

import com.veterinaria.gestion.model.Cliente;
import com.veterinaria.gestion.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes/lista-cliente"; // templates/clientes/lista.html
    }
    
    @GetMapping("/buscar")
    public String buscarPorDni(@RequestParam("dni") String dni, Model model) {
        Cliente cliente = clienteService.buscarPorDni(dni);
        
        if (cliente != null) {
            model.addAttribute("cliente", cliente);
        } else {
            model.addAttribute("error", "No se encontró ningún cliente con el DNI: " + dni);
        }
        
        // Volvemos a la lista o a una página de detalle
        return "clientes/lista"; 
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/formulario-cliente";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        // Asumiendo que tienes un método buscarPorId en tu Service
        Cliente cliente = clienteService.buscarPorId(id); 
        model.addAttribute("cliente", cliente);
        return "clientes/formulario-cliente";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable("id") Long id) {
        clienteService.eliminar(id);
        return "redirect:/clientes";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente) {
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }
}