package com.veterinaria.gestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.veterinaria.gestion.model.Producto;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // OBLIGATORIA: Mostrar solo lo que hay en stock
    List<Producto> findByStockGreaterThan(Integer cantidad);

    // OPCIONAL: Filtro por categorías (Pienso, Juguetes...)
    List<Producto> findByCategoria(String categoria);
}