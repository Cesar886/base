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
    public String agregarAlCarrito(@RequestParam("id") Long id, 
                                  @RequestParam("cantidad") int cantidad, 
                                  HttpSession session) {
        
        Producto producto = productoService.obtenerPorId(id);
        if (producto == null) {
            return "redirect:/inicio";
        }

        // Manejo seguro del carrito en sesión
        List<ItemCarrito> carrito;
        Object carritoObj = session.getAttribute("carrito");

        if (carritoObj instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<ItemCarrito> temp = (List<ItemCarrito>) carritoObj;
            carrito = temp;
        } else {
            carrito = new ArrayList<>();
        }

        // Verificar si el producto ya existe en el carrito
        boolean productoExistente = false;
        for (ItemCarrito item : carrito) {
            if (item.getProducto().getId().equals(id)) {
                item.setCantidad(item.getCantidad() + cantidad);
                productoExistente = true;
                break;
            }
        }

        // Agregar nuevo item si no existe
        if (!productoExistente) {
            carrito.add(new ItemCarrito(producto, cantidad));
        }

        // Actualizar sesión
        session.setAttribute("carrito", carrito);
        return "redirect:/inicio";
    }
}