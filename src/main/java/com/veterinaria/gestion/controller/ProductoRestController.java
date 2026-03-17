package com.veterinaria.gestion.controller;

import com.veterinaria.gestion.model.Producto;
import com.veterinaria.gestion.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos") // Todas las rutas empezarán por /api/productos
public class ProductoRestController {

    @Autowired
    private ProductoService productoService;

    // 1. OBTENER TODOS: GET http://localhost:8080/api/productos
    @GetMapping
    public List<Producto> listarTodos() {
        return productoService.obtenerTodos();
    }

    // 2. OBTENER UNO: GET http://localhost:8080/api/productos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. CREAR: POST http://localhost:8080/api/productos
    // Envía un JSON en el cuerpo de la petición
    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        return productoService.guardar(producto);
    }

    // 4. ACTUALIZAR: PUT http://localhost:8080/api/productos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.obtenerPorId(id)
            .map(p -> {
                producto.setId(id);
                return ResponseEntity.ok(productoService.guardar(producto));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // 5. BORRAR: DELETE http://localhost:8080/api/productos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
