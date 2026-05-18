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

        // Buscar el cliente asociado al usuario
        Cliente cliente = clienteRepository.findByUsuarioUsername(username).orElse(null);
        if (cliente == null) {
            cliente = clienteRepository.findAll().stream().findFirst().orElse(null);
        }

        if (cliente == null) {
            throw new RuntimeException("No existe ningún cliente registrado. Debe haber al menos uno.");
        }


        java.math.BigDecimal costeTotal = java.math.BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> entry : carrito.entrySet()) {
            Long productoId = entry.getKey();
            Integer cantidad = entry.getValue();
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoId));
            
            if (producto.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }
            costeTotal = costeTotal.add(producto.getPrecio().multiply(java.math.BigDecimal.valueOf(cantidad)));
        }


        if (cliente.getSaldo() == null) {
            cliente.setSaldo(java.math.BigDecimal.ZERO);
        }
        if (cliente.getSaldo().compareTo(costeTotal) < 0) {
            throw new RuntimeException("Saldo insuficiente. Tienes " + cliente.getSaldo() + "€ y necesitas " + costeTotal + "€");
        }


        cliente.setSaldo(cliente.getSaldo().subtract(costeTotal));
        clienteRepository.save(cliente);


        for (Map.Entry<Long, Integer> entry : carrito.entrySet()) {
            Long productoId = entry.getKey();
            Integer cantidad = entry.getValue();

            Producto producto = productoRepository.findById(productoId).get();


            for (int i = 0; i < cantidad; i++) {
                Pedido compra = new Pedido();
                compra.setCliente(cliente);
                compra.setProducto(producto);
                compra.setPrecioPagado(producto.getPrecio());
                pedidoRepository.save(compra);
            }


            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);
        }
    }
}
