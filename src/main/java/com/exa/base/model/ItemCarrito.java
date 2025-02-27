package com.exa.base.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class ItemCarrito implements Serializable {
    private Producto producto;
    private int cantidad;
}
