package com.veterinaria.gestion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
public class Cliente {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "Formato de DNI inválido")
    @Column(unique = true, nullable = false)
    private String dni;

    private String telefono; 
    
    @Email(message = "Debe ser un correo válido") // Valida formato si se escribe algo, pero permite nulo
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(message = "El usuario asociado no puede ser nulo")
    private Usuario usuario;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Mascota> mascotas;
}