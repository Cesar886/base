package com.exa.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.ArrayList;

@Controller
public class InicioController {

    @GetMapping("/inicio")
    public String inicio(Model model) {
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto(1L, "ASUS Zenbook", "Potente laptop para trabajo", 1200.00, "asuss.webp"));
        productos.add(new Producto(2L, "Smartphone Samsung", "6.9-inch* QHD+ Dynamic AMOLED 2X Display Vision booster Adaptive color tone", 900.00, "GalaxyS25Ultra.jpg"));
        productos.add(new Producto(3L, "Audífonos Inalámbricos", "Calidad de sonido premium", 150.00, "galaxy-buds3.avif"));
        productos.add(new Producto(1L, "Laptop Dell", "Potente laptop para trabajo", 1200.00, "asuss.webp"));
        productos.add(new Producto(2L, "Smartphone Samsung", "6.9-inch* QHD+ Dynamic AMOLED 2X Display Vision booster Adaptive color tone", 900.00, "GalaxyS25Ultra.jpg"));
        productos.add(new Producto(3L, "Audífonos Inalámbricos", "Calidad de sonido premium", 150.00, "galaxy-buds3.avif"));
        productos.add(new Producto(1L, "Laptop Dell", "Potente laptop para trabajo", 1200.00, "asuss.webp"));
        productos.add(new Producto(2L, "Smartphone Samsung", "6.9-inch* QHD+ Dynamic AMOLED 2X Display Vision booster Adaptive color tone", 900.00, "GalaxyS25Ultra.jpg"));
        productos.add(new Producto(3L, "Audífonos Inalámbricos", "Calidad de sonido premium", 150.00, "galaxy-buds3.avif"));
    
        model.addAttribute("productos", productos);
        return "inicio";
    }
    

    // Clase interna para Producto (Si no tienes un modelo)
    class Producto {
        private Long id;
        private String nombre;
        private String descripcion;
        private Double precio;
        private String imagenUrl;

        public Producto(Long id, String nombre, String descripcion, Double precio, String imagenUrl) {
            this.id = id;
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.precio = precio;
            this.imagenUrl = imagenUrl;
        }

        public Long getId() { return id; }
        public String getNombre() { return nombre; }
        public String getDescripcion() { return descripcion; }
        public Double getPrecio() { return precio; }
        public String getImagenUrl() { return imagenUrl; }
    }
}
