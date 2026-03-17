package com.veterinaria.gestion.service;

import com.veterinaria.gestion.model.Producto;
import com.veterinaria.gestion.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // LEER TODO
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // LEER UNO POR ID
    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    // CREAR O ACTUALIZAR (CRUD completo)
    @Transactional
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    // BORRAR
    @Transactional
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
}