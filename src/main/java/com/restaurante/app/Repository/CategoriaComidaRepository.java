package com.restaurante.app.Repository;

import com.restaurante.app.Entity.CategoriaComida;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoriaComidaRepository extends MongoRepository<CategoriaComida, String> {
    Optional<CategoriaComida> findByNombre(String nombre);

}
