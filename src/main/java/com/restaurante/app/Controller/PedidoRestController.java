package com.restaurante.app.Controller;

import com.restaurante.app.Entity.Pedido;
import com.restaurante.app.Exception.NotFoundException;
import com.restaurante.app.Repository.PedidoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoRestController {

    @Autowired
    private PedidoRepository pedidoRepository;

    // Obtener todos los pedidos
    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    // Obtener un pedido por ID
    @GetMapping("/{id}")
    public Pedido getPedidoById(@PathVariable String id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido no encontrado con id: " + id));
    }

    // Crear un nuevo pedido
    @PostMapping
    public ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) {
        Pedido nuevoPedido = pedidoRepository.save(pedido);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    // Actualizar un pedido existente
    @PutMapping("/{id}")
    public Pedido updatePedido(@PathVariable String id, @RequestBody Pedido pedidoActualizado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido no encontrado con id: " + id));

        pedido.setClienteId(pedidoActualizado.getClienteId());
        pedido.setNombreCliente(pedidoActualizado.getNombreCliente());
        pedido.setRestauranteId(pedidoActualizado.getRestauranteId());
        pedido.setNombreRestaurante(pedidoActualizado.getNombreRestaurante());
        pedido.setComidaId(pedidoActualizado.getComidaId());
        pedido.setNombreComida(pedidoActualizado.getNombreComida());
        pedido.setTotal(pedidoActualizado.getTotal());
        pedido.setEstado(pedidoActualizado.getEstado());
        pedido.setFechaPedido(pedidoActualizado.getFechaPedido());

        return pedidoRepository.save(pedido);
    }

    // Eliminar un pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePedido(@PathVariable String id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido no encontrado con id: " + id));

        pedidoRepository.delete(pedido);
        return new ResponseEntity<>("Pedido eliminado exitosamente", HttpStatus.OK);
    }
}
