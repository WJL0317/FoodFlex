package com.restaurante.app.Controller;

import com.restaurante.app.Entity.Restaurante;
import com.restaurante.app.Exception.NotFoundException;
import com.restaurante.app.Repository.RestauranteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteRestController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    // Obtener todos los restaurantes
    @GetMapping
    public List<Restaurante> getAllRestaurantes() {
        return restauranteRepository.findAll();
    }

    // Obtener un restaurante por ID
    @GetMapping("/{id}")
    public Restaurante getRestauranteById(@PathVariable String id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurante no encontrado con id: " + id));
    }

    // Crear un nuevo restaurante
    @PostMapping
    public ResponseEntity<Restaurante> createRestaurante(@RequestBody Restaurante restaurante) {
        Restaurante nuevoRestaurante = restauranteRepository.save(restaurante);
        return new ResponseEntity<>(nuevoRestaurante, HttpStatus.CREATED);
    }

    // Actualizar un restaurante existente
    @PutMapping("/{id}")
    public Restaurante updateRestaurante(@PathVariable String id, @RequestBody Restaurante restauranteActualizado) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurante no encontrado con id: " + id));

        restaurante.setNombre(restauranteActualizado.getNombre());
        restaurante.setDireccion(restauranteActualizado.getDireccion());
        restaurante.setTelefono(restauranteActualizado.getTelefono());
        restaurante.setEmail(restauranteActualizado.getEmail());
        restaurante.setDescripcion(restauranteActualizado.getDescripcion());
        restaurante.setUsuario(restauranteActualizado.getUsuario());
        restaurante.setContrasena(restauranteActualizado.getContrasena());

        return restauranteRepository.save(restaurante);
    }

    // Eliminar un restaurante
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRestaurante(@PathVariable String id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurante no encontrado con id: " + id));

        restauranteRepository.delete(restaurante);
        return new ResponseEntity<>("Restaurante eliminado exitosamente", HttpStatus.OK);
    }
}
