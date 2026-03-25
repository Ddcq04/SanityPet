package com.veterinaria.gestion.service;

import com.veterinaria.gestion.model.Mascota;
import com.veterinaria.gestion.model.Cliente;
import com.veterinaria.gestion.repository.MascotaRepository;
import com.veterinaria.gestion.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;

    public List<Mascota> listarTodas() {
        return mascotaRepository.findAll();
    }

    public List<Mascota> buscarPorDueño(Long clienteId) {
        return mascotaRepository.findByClienteId(clienteId);
    }

    // --- NUEVO: Para que el Cliente vea las suyas usando su DNI (login) ---
    public List<Mascota> buscarMisMascotas(String dni) {
        Cliente cliente = clienteRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con DNI: " + dni));
        return mascotaRepository.findByClienteId(cliente.getId());
    }

    public void guardar(Mascota mascota) {
        mascotaRepository.save(mascota);
    }

    public List<Mascota> buscarPorEspecie(String especie) {
        return mascotaRepository.findByEspecie(especie);
    }
    
    public List<Mascota> buscarPorRaza(String especie) {
        return mascotaRepository.findByRaza(especie);
    }
}