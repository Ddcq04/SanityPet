package com.veterinaria.gestion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.veterinaria.gestion.model.Usuario;
import com.veterinaria.gestion.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Usamos tu consulta para buscar al usuario
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario: " + username));

        // 2. Le devolvemos a Spring un objeto "UserDetails" con los datos que encontró
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword()) // La contraseña que está en la DB
                .roles(usuario.getRol())        // El rol (ADMIN, VET, etc.)
                .build();
    }
}