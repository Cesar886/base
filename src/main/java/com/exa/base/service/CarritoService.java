package com.exa.base.service;

import com.exa.base.model.Carrito;
import com.exa.base.model.ItemCarrito;
import com.exa.base.model.Producto;
import com.exa.base.repository.CarritoRepository;
import com.exa.base.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public void agregarProducto(Long usuarioId, Long productoId, int cantidad) {
        // Buscar o crear carrito del usuario
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuarioId(usuarioId);
                    return carritoRepository.save(nuevoCarrito);
                });

        // Buscar producto
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Verificar si el producto ya está en el carrito
        Optional<ItemCarrito> itemExistente = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst();

        if (itemExistente.isPresent()) {
            // Actualizar cantidad
            ItemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
        } else {
            // Crear nuevo item
            ItemCarrito newItem = new ItemCarrito();
            newItem.setCarrito(carrito);
            newItem.setProducto(producto);
            newItem.setCantidad(cantidad);
            carrito.getItems().add(newItem);
        }

        carritoRepository.save(carrito);
    }

    public Optional<Carrito> obtenerCarritoPorUsuario(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }
}