package com.exa.base.controller;

import com.exa.base.model.ItemCarrito;
import com.exa.base.model.Producto;
import com.exa.base.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final ProductoService productoService;

    @Autowired
    public CarritoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam("id") Long id, @RequestParam("cantidad") int cantidad, HttpSession session) {
        Producto producto = productoService.obtenerPorId(id);

        if (producto == null) {
            return "redirect:/inicio"; // Si el producto no existe, redirige
        }

        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        boolean productoExistente = false;
        for (ItemCarrito item : carrito) {
            if (item.getProducto().getId().equals(id)) {
                item.setCantidad(item.getCantidad() + cantidad);
                productoExistente = true;
                break;
            }
        }

        if (!productoExistente) {
            carrito.add(new ItemCarrito(producto, cantidad));
        }

        session.setAttribute("carrito", carrito);
        return "redirect:/inicio";
    }
}
