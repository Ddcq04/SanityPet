package com.veterinaria.gestion.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "mascotas")
@Data
@NoArgsConstructor
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String especie;
    private String raza;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @ToString.Exclude
    private Cliente cliente;
    
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Cita> citas;
    
    // Método para obtner cantidad total de citas
    public int getCantidadCitas() {
        return (citas != null) ? citas.size() : 0;
    }
}
