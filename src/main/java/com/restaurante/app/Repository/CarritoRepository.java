package com.restaurante.app.Repository;

import com.restaurante.app.Entity.Carrito;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CarritoRepository extends MongoRepository<Carrito, String> {

    // Método para buscar carritos por clienteId
    Optional<Carrito> findByClienteId(String clienteId);
}
