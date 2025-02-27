package com.exa.base.service;

import com.exa.base.model.Producto;
import java.util.List;

public interface ProductoService {
    Producto obtenerPorId(Long id);
    List<Producto> obtenerTodos();
}
