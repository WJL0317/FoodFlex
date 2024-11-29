package com.restaurante.app.Repository;

import com.restaurante.app.Entity.Comida;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ComidaRepository extends MongoRepository<Comida, String> {
   
}
