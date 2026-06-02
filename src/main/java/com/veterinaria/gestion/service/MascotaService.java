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

    //Para que el Cliente vea las suyas usando su DNI
    public List<Mascota> buscarMisMascotas(String dni) {
        Cliente cliente = clienteRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con DNI: " + dni));
        return mascotaRepository.findByClienteId(cliente.getId());
    }
 // Para cargar la mascota en el formulario de edición
    public Mascota buscarPorId(Long id) {
        return mascotaRepository.findById(id).orElse(null);
    }
    public Mascota buscarPorIdSeguro(Long id, String username, boolean isAdmin) {
        Mascota mascota = buscarPorId(id);
        if (mascota != null && !isAdmin && !mascota.getCliente().getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("Acceso denegado a esta mascota.");
        }
        return mascota;
    }

    // Para insertar o actualizar la mascota
    public void guardar(Mascota mascota) {
        mascotaRepository.save(mascota);
    }
    
    public void eliminar(Long id) {
        mascotaRepository.deleteById(id);
    }
    
    public List<Mascota> buscarFiltrado(String nombre, String especie, String raza, String dueno) {
    	String n = (nombre == null || nombre.isBlank()) ? null : nombre;
        String e = (especie == null || especie.isBlank()) ? null : especie;
        String r = (raza == null || raza.isBlank()) ? null : raza;
        String d = (dueno == null || dueno.isBlank()) ? null : dueno;
        // Ejecuta la búsqueda combinada
        return mascotaRepository.buscarConFiltros(n, e, r, d);
    }
}