package com.veterinaria.gestion.service;

import com.veterinaria.gestion.model.*;
import com.veterinaria.gestion.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public void realizarPedido(String username, Map<Long, Integer> carrito) {
        if (carrito == null || carrito.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // Buscar el cliente asociado al usuario (con la lógica dummy que añadimos antes)
        Cliente cliente = clienteRepository.findByUsuarioUsername(username).orElse(null);
        if (cliente == null) {
            cliente = clienteRepository.findAll().stream().findFirst().orElse(null);
        }

        if (cliente == null) {
            throw new RuntimeException("No existe ningún cliente registrado. Debe haber al menos uno.");
        }

        // En esta estructura "plana", guardamos una fila por cada producto comprado
        for (Map.Entry<Long, Integer> entry : carrito.entrySet()) {
            Long productoId = entry.getKey();
            Integer cantidad = entry.getValue();

            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoId));

            if (producto.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            // Si compró más de 1 unidad del mismo producto, podemos guardar N filas o 
            // adaptar la tabla a tener "cantidad". Pero según la imagen del usuario, 
            // no hay columna 'cantidad', así que guardamos N filas para ser fieles a su esquema.
            for (int i = 0; i < cantidad; i++) {
                Pedido compra = new Pedido();
                compra.setCliente(cliente);
                compra.setProducto(producto);
                compra.setPrecioPagado(producto.getPrecio());
                pedidoRepository.save(compra);
            }

            // Actualizar stock de verdad
            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);
        }
    }
}
