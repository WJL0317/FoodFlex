package com.restaurante.app.Controller;

import com.restaurante.app.Entity.CategoriaComida;
import com.restaurante.app.Exception.NotFoundException;
import com.restaurante.app.Repository.CategoriaComidaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias-comida")
public class CategoriaComidaRestController {

    @Autowired
    private CategoriaComidaRepository categoriaComidaRepository;

    // Obtener todas las categorías
    @GetMapping
    public List<CategoriaComida> getAllCategoriasComida() {
        return categoriaComidaRepository.findAll();
    }

    // Obtener una categoría por ID
    @GetMapping("/{id}")
    public CategoriaComida getCategoriaComidaById(@PathVariable String id) {
        return categoriaComidaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría de comida no encontrada con id: " + id));
    }

    // Crear una nueva categoría
    @PostMapping
    public ResponseEntity<CategoriaComida> createCategoriaComida(@RequestBody CategoriaComida categoriaComida) {
        CategoriaComida nuevaCategoria = categoriaComidaRepository.save(categoriaComida);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    // Actualizar una categoría existente
    @PutMapping("/{id}")
    public CategoriaComida updateCategoriaComida(@PathVariable String id, @RequestBody CategoriaComida categoriaActualizada) {
        CategoriaComida categoria = categoriaComidaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría de comida no encontrada con id: " + id));

        categoria.setNombre(categoriaActualizada.getNombre());
        categoria.setDescripcion(categoriaActualizada.getDescripcion());

        return categoriaComidaRepository.save(categoria);
    }

    // Eliminar una categoría
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoriaComida(@PathVariable String id) {
        CategoriaComida categoria = categoriaComidaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría de comida no encontrada con id: " + id));

        categoriaComidaRepository.delete(categoria);
        return new ResponseEntity<>("Categoría de comida eliminada exitosamente", HttpStatus.OK);
    }
}
