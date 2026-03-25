package com.veterinaria.gestion.service;

import com.veterinaria.gestion.model.Cliente;
import com.veterinaria.gestion.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public void guardar(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    public Cliente buscarPorDni(String dni) {
        return clienteRepository.findByDni(dni).orElse(null);
    }
    
    public Cliente buscarPorId(Long id) {
        // findById devuelve un Optional, lo gestionamos con orElse(null)
        return clienteRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        // Borrado directo por la clave primaria
        clienteRepository.deleteById(id);
    }
    
}