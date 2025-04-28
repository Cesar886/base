package com.exa.base.repository;

import com.exa.base.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuarioId(Long usuarioId); // ¡Método correcto!
}