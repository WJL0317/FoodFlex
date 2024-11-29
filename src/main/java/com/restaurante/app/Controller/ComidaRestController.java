package com.restaurante.app.Controller;

import com.restaurante.app.Entity.Comida;
import com.restaurante.app.Exception.NotFoundException;
import com.restaurante.app.Repository.ComidaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comidas")
public class ComidaRestController {

    @Autowired
    private ComidaRepository comidaRepository;

    // Obtener todas las comidas
    @GetMapping
    public List<Comida> getAllComidas() {
        return comidaRepository.findAll();
    }

    // Obtener una comida por ID
    @GetMapping("/{id}")
    public Comida getComidaById(@PathVariable String id) {
        return comidaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comida no encontrada con id: " + id));
    }

    // Crear una nueva comida
    @PostMapping
    public ResponseEntity<Comida> createComida(@RequestBody Comida comida) {
        Comida nuevaComida = comidaRepository.save(comida);
        return new ResponseEntity<>(nuevaComida, HttpStatus.CREATED);
    }

    // Actualizar una comida existente
    @PutMapping("/{id}")
    public Comida updateComida(@PathVariable String id, @RequestBody Comida comidaActualizada) {
        Comida comida = comidaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comida no encontrada con id: " + id));

        comida.setNombre(comidaActualizada.getNombre());
        comida.setDescripcion(comidaActualizada.getDescripcion());
        comida.setPrecio(comidaActualizada.getPrecio());
        comida.setRestauranteNombre(comidaActualizada.getRestauranteNombre());
        comida.setCategoriaNombre(comidaActualizada.getCategoriaNombre());
        comida.setImagenUrl(comidaActualizada.getImagenUrl());

        return comidaRepository.save(comida);
    }

    // Eliminar una comida
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComida(@PathVariable String id) {
        Comida comida = comidaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comida no encontrada con id: " + id));

        comidaRepository.delete(comida);
        return new ResponseEntity<>("Comida eliminada exitosamente", HttpStatus.OK);
    }
}
