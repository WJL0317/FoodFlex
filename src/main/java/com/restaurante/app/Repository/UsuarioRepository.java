package com.restaurante.app.Repository;

import com.restaurante.app.Entity.Usuario;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
	@Query("{ 'rol': { $regex: ?0, $options: 'i' } }")
    List<Usuario> findAllByRol(String rol);
}
