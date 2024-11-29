package com.restaurante.app.Repository;

import com.restaurante.app.Entity.Restaurante;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RestauranteRepository extends MongoRepository<Restaurante, String> {

    // Búsqueda insensible a mayúsculas y minúsculas
    @Query("{ 'nombre': { $regex: ?0, $options: 'i' } }")
    Optional<Restaurante> findByNombreIgnoreCase(String nombre);
    
    Optional<Restaurante> findByNombre(String nombre);

    
}