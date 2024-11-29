package com.restaurante.app.Controller;

import com.restaurante.app.Entity.Carrito;
import com.restaurante.app.Exception.NotFoundException;
import com.restaurante.app.Repository.CarritoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carritos")
public class CarritoRestController {

    @Autowired
    private CarritoRepository carritoRepository;

    // Obtener todos los carritos
    @GetMapping
    public List<Carrito> getAllCarritos() {
        return carritoRepository.findAll();
    }

    // Obtener un carrito por ID
    @GetMapping("/{id}")
    public Carrito getCarritoById(@PathVariable String id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Carrito no encontrado con id: " + id));
    }

    // Crear un nuevo carrito
    @PostMapping
    public ResponseEntity<Carrito> createCarrito(@RequestBody Carrito carrito) {
        Carrito nuevoCarrito = carritoRepository.save(carrito);
        return new ResponseEntity<>(nuevoCarrito, HttpStatus.CREATED);
    }

    // Actualizar un carrito existente
    @PutMapping("/{id}")
    public Carrito updateCarrito(@PathVariable String id, @RequestBody Carrito carritoActualizado) {
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Carrito no encontrado con id: " + id));

        carrito.setClienteId(carritoActualizado.getClienteId());
        carrito.setNombreCliente(carritoActualizado.getNombreCliente());
        carrito.setPedido(carritoActualizado.getPedido());

        return carritoRepository.save(carrito);
    }

    // Eliminar un carrito
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCarrito(@PathVariable String id) {
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Carrito no encontrado con id: " + id));

        carritoRepository.delete(carrito);
        return new ResponseEntity<>("Carrito eliminado exitosamente", HttpStatus.OK);
    }
}
