package com.exa.base.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ItemCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CARRITO_ID")
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "PRODUCTO_ID")
    private Producto producto;

    private int cantidad;
}