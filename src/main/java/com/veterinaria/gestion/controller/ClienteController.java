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
        return "clientes/lista-cliente"; // templates/clientes/lista.html
    }
    
    @GetMapping("/buscar")
    public String buscar(@RequestParam("busqueda") String termino, Model model) {
        List<Cliente> resultados = clienteService.buscarPorNombre(termino);
        model.addAttribute("clientes", resultados);
        return "clientes/lista-cliente";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        Cliente cliente = new Cliente();
        cliente.setUsuario(new Usuario()); // IMPORTANTE: Inicializa el usuario para que el HTML no de error
        model.addAttribute("cliente", cliente);
        return "clientes/formulario-cliente";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        // Asumiendo que tienes un método buscarPorId en tu Service
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

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, Model model) {
        
        Usuario usuario = cliente.getUsuario();

        // 1. Validar si las contraseñas coinciden
        if (cliente.getId() == null && (usuario.getPasswordPlana() == null || usuario.getPasswordPlana().isEmpty())) {
            result.rejectValue("usuario.passwordPlana", "error.usuario", "La contraseña es obligatoria para nuevos clientes");
        }
        
        if (usuario.getPasswordPlana() != null && !usuario.getPasswordPlana().isEmpty()) {
	        if (!usuario.getPasswordPlana().equals(usuario.getPasswordRepeat())) {
	        	result.rejectValue("usuario.passwordRepeat", "error.usuario", "Las contraseñas no coinciden");
	        }
        }

        // 2. Si hay errores, volver al formulario
        if (result.hasErrors()) {
            return "clientes/formulario-cliente";
        }

        // 3. Lógica de guardado
        if (cliente.getId() == null) {
            // NUEVO CLIENTE
            usuario.setRol("user");
            usuario.setUsername(cliente.getDni());
            usuario.setPassword(passwordEncoder.encode(usuario.getPasswordPlana()));
        } else {
            // EDICIÓN DE CLIENTE
            Cliente clienteExistente = clienteService.buscarPorId(cliente.getId());
            
            if (clienteExistente != null) {
                // ¡ESTO ES LO IMPORTANTE! Forzamos el ID del usuario existente
                if (clienteExistente.getUsuario() != null) {
                    usuario.setId(clienteExistente.getUsuario().getId());
                    
                    // Mantenemos la contraseña si no se ha escrito una nueva
                    if (usuario.getPasswordPlana() == null || usuario.getPasswordPlana().isEmpty()) {
                        usuario.setPassword(clienteExistente.getUsuario().getPassword());
                    } else {
                        usuario.setPassword(passwordEncoder.encode(usuario.getPasswordPlana()));
                    }
                    
                    // Mantenemos el rol original
                    usuario.setRol(clienteExistente.getUsuario().getRol());
                }
                // Actualizamos el username por si cambió el DNI
                usuario.setUsername(cliente.getDni());
            }
        }

        // 4. Guardar
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }
    
    @GetMapping("/hacer-admin/{id}")
    public String promoverAAdmin(@PathVariable("id") Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        
        if (cliente != null && cliente.getUsuario() != null) {
            // Cambiamos el rol a "admin" (asegúrate de que coincida con tu SecurityConfig)
            cliente.getUsuario().setRol("admin");
            
            // Guardamos solo el cambio de rol
            clienteService.guardar(cliente);
        }
        
        return "redirect:/clientes";
    }
}