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
    
    @Pattern(
            regexp = "^\\s*$|^(?:[+\\-() .]*\\d){9,}[+\\-() .]*$", 
            message = "El teléfono debe tener un formato válido"
        )
    private String telefono; 
    
    @Email(message = "Por favor, introduce una dirección de correo válida")
    @Pattern(
            regexp = "^$|^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", 
            message = "El formato del correo debe ser completo (ejemplo: usuario@dominio.com)"
     )
    private String email;

    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private java.math.BigDecimal saldo = java.math.BigDecimal.ZERO;


    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(message = "El usuario asociado no puede ser nulo")
    private Usuario usuario;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Mascota> mascotas;
}