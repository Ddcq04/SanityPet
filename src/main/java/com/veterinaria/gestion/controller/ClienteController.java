package com.veterinaria.gestion.controller;

import com.veterinaria.gestion.model.Cliente;
import com.veterinaria.gestion.model.Usuario;
import com.veterinaria.gestion.service.ClienteService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes/lista-cliente"; 
    }
    
    @GetMapping("/buscar")
    public String buscar(@RequestParam("busqueda") String termino, Model model) {
        List<Cliente> resultados = clienteService.buscarPorNombre(termino);
        model.addAttribute("clientes", resultados);
        return "clientes/lista-cliente";
    }

    // Formulario de creación desde la vista del Administrador
    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        Cliente cliente = new Cliente();
        cliente.setUsuario(new Usuario()); 
        model.addAttribute("cliente", cliente);
        return "clientes/formulario-cliente";
    }
    
    // Formulario de edición desde la vista del Administrador
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id); 
        if (cliente == null) {
            return "redirect:/clientes";
        }
        model.addAttribute("cliente", cliente);
        return "clientes/formulario-cliente";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable("id") Long id) {
        clienteService.eliminar(id);
        return "redirect:/clientes";
    }

    // Procesar el formulario de guardado del Administrador
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, Model model) {
        
        Usuario usuario = cliente.getUsuario();

        // 1. Si es una creación interna del Admin, las contraseñas son obligatorias
        if (cliente.getId() == null) {
            if (usuario.getPasswordPlana() == null || usuario.getPasswordPlana().isEmpty()) {
                result.rejectValue("usuario.passwordPlana", "error.usuario", "La contraseña es obligatoria para nuevos clientes");
            }
            if (usuario.getPasswordPlana() != null && !usuario.getPasswordPlana().equals(usuario.getPasswordRepeat())) {
                result.rejectValue("usuario.passwordRepeat", "error.usuario", "Las contraseñas no coinciden");
            }
            
            // 🌟 NUEVO: Comprobar DNI duplicado SOLO al crear (si el DNI ya existe en otro cliente)
            if (cliente.getDni() != null && !cliente.getDni().trim().isEmpty()) {
                Cliente clienteExistente = clienteService.buscarPorDni(cliente.getDni());
                if (clienteExistente != null) {
                    result.rejectValue("dni", "error.cliente", "El DNI introducido ya pertenece a otro cliente.");
                }
            }
        } else {
            // 🌟 NUEVO: Comprobar DNI duplicado al EDITAR (asegurarnos de que el nuevo DNI no es de otro cliente distinto)
            if (cliente.getDni() != null && !cliente.getDni().trim().isEmpty()) {
                Cliente clienteExistente = clienteService.buscarPorDni(cliente.getDni());
                // Si encontramos un cliente con ese DNI, pero NO es el cliente que estamos editando, es un error
                if (clienteExistente != null && !clienteExistente.getId().equals(cliente.getId())) {
                    result.rejectValue("dni", "error.cliente", "Este DNI ya está asignado a otro cliente en el sistema.");
                }
            }
        }

        // 2. Comprobamos errores de validación
        if (result.hasErrors()) {
            model.addAttribute("fromAdmin", true);
            return "clientes/formulario-cliente";
        }

        // 3. Lógica de persistencia
        if (cliente.getId() == null) {
            usuario.setRol("user");
            usuario.setUsername(cliente.getDni());
            usuario.setPassword(passwordEncoder.encode(usuario.getPasswordPlana()));
        } else {
            Cliente clienteExistenteBD = clienteService.buscarPorId(cliente.getId());
            if (clienteExistenteBD != null && clienteExistenteBD.getUsuario() != null) {
                usuario.setId(clienteExistenteBD.getUsuario().getId());
                usuario.setPassword(clienteExistenteBD.getUsuario().getPassword());
                usuario.setRol(clienteExistenteBD.getUsuario().getRol());
            }
            usuario.setUsername(cliente.getDni());
        }

        // 4. Guardado seguro
        try {
            clienteService.guardar(cliente);
        } catch (Exception e) {
            // Si algo falla al final (por concurrencia, etc), devolvemos al formulario amigablemente
            result.rejectValue("dni", "error.cliente", "Error interno al guardar. DNI posiblemente duplicado.");
            model.addAttribute("fromAdmin", true);
            return "clientes/formulario-cliente";
        }
        
        return "redirect:/clientes?creado=true";
    }
    
    @GetMapping("/hacer-admin/{id}")
    public String promoverAAdmin(@PathVariable("id") Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        if (cliente != null && cliente.getUsuario() != null) {
            cliente.getUsuario().setRol("admin");
            clienteService.guardar(cliente);
        }
        return "redirect:/clientes";
    }
}