package com.veterinaria.gestion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*; 
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data //Sustituye Getters, Setters, toString
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false)
    private String password;
    
    @Transient // No se guarda en la DB
    private String passwordRepeat;

    @Transient // Opcional: para manejar la password en texto plano antes de cifrar
    private String passwordPlana;

    @NotBlank
    private String rol; // ROLE_ADMIN o ROLE_USER
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre_completo")
    private String nombreCompleto;
}