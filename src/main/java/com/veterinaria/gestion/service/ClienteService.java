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

    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByUsuarioNombreCompletoContainingIgnoreCase(nombre);
    }
    
    public Cliente buscarPorDni(String dni) {
        return clienteRepository.findByDni(dni).orElse(null);
    }
    
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }
    
}