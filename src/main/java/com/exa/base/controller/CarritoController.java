package com.exa.base.controller;

import com.exa.base.model.Carrito;
import com.exa.base.model.Producto;
import com.exa.base.model.Usuario;
import com.exa.base.service.CarritoService;
import org.springframework.web.bind.annotation.RequestMapping;
import com.exa.base.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CarritoService carritoService;

    // Agregar producto al carrito (POST)
    @PostMapping("/agregar")
    public String agregarAlCarrito(
            @RequestParam("id") Long productoId,
            @RequestParam("cantidad") int cantidad,
            Authentication authentication) {

        // Verificar autenticación
        if (!authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // Obtener usuario actual
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // Buscar producto
        Producto producto = productoService.obtenerPorId(productoId);
        if (producto == null) {
            return "redirect:/inicio?error=producto_no_encontrado";
        }

        // Agregar al carrito
        carritoService.agregarProducto(usuario.getId(), productoId, cantidad);
        return "redirect:/inicio?success=producto_agregado";
    }

    // Mostrar vista del carrito (GET)
    @GetMapping
    public String verCarrito(Authentication authentication, Model model) {
        // Verificar autenticación
        if (!authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // Obtener usuario actual
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // Obtener carrito desde la base de datos
        Carrito carrito = carritoService.obtenerCarritoPorUsuario(usuario.getId())
                .orElse(new Carrito());

        // Pasar datos a la vista
        model.addAttribute("carrito", carrito);
        return "carrito"; // Debe existir carrito.html en src/main/resources/templates
    }
}