package com.restaurante.app.Controller;

import com.restaurante.app.Entity.Carrito;
import com.restaurante.app.Entity.Comida;
import com.restaurante.app.Entity.Usuario;
import com.restaurante.app.Repository.CarritoRepository;
import com.restaurante.app.Repository.ComidaRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/carrito")
public class CarritoWebController {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ComidaRepository comidaRepository;

    @PostMapping("/agregar")
    public ResponseEntity<String> agregarAlCarrito(@RequestBody Map<String, String> body, HttpSession session) {
        String comidaId = body.get("comidaId");
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || !"cliente".equalsIgnoreCase(usuario.getRol())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
        }

        Optional<Carrito> carritoOptional = carritoRepository.findByClienteId(usuario.getId());
        Carrito carrito = carritoOptional.orElse(new Carrito());
        carrito.setClienteId(usuario.getId());
        carrito.setNombreCliente(usuario.getNombre());

        List<String> pedidoActual = carrito.getPedido() == null ? new ArrayList<>() : carrito.getPedido();
        if (!pedidoActual.contains(comidaId)) { // Evita duplicados
            pedidoActual.add(comidaId);
        }
        carrito.setPedido(pedidoActual);

        carritoRepository.save(carrito);
        return ResponseEntity.ok("Comida añadida al carrito");
    }

    @PostMapping("/actualizar-cantidad")
    public ResponseEntity<Map<String, String>> actualizarCantidad(@RequestBody Map<String, String> body, HttpSession session) {
        String comidaId = body.get("comidaId");
        String nuevaCantidadStr = body.get("cantidad");

        if (comidaId == null || comidaId.isEmpty() || nuevaCantidadStr == null || nuevaCantidadStr.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", "false", "message", "Datos inválidos"));
        }

        int nuevaCantidad;
        try {
            nuevaCantidad = Integer.parseInt(nuevaCantidadStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", "false", "message", "Cantidad inválida"));
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"cliente".equalsIgnoreCase(usuario.getRol())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", "false", "message", "No autorizado"));
        }

        Optional<Carrito> carritoOptional = carritoRepository.findByClienteId(usuario.getId());
        if (carritoOptional.isPresent()) {
            Carrito carrito = carritoOptional.get();
            List<String> pedidoActual = carrito.getPedido();

            if (pedidoActual != null) {
                for (int i = 0; i < pedidoActual.size(); i++) {
                    String[] parts = pedidoActual.get(i).split("\\|");
                    if (parts.length >= 2 && parts[0].equals(comidaId)) {
                        String precio = parts[1];
                        pedidoActual.set(i, comidaId + "|" + precio + "|" + nuevaCantidad); // Actualizar cantidad
                        break;
                    }
                }
                carrito.setPedido(pedidoActual);
                carritoRepository.save(carrito);
                return ResponseEntity.ok(Map.of("success", "true", "message", "Cantidad actualizada"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", "false", "message", "Carrito no encontrado"));
    }

    @GetMapping
    public String verCarrito(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || !"cliente".equalsIgnoreCase(usuario.getRol())) {
            return "redirect:/login";
        }

        Optional<Carrito> carritoOptional = carritoRepository.findByClienteId(usuario.getId());
        Carrito carrito = carritoOptional.orElse(new Carrito());
        List<Comida> comidas = new ArrayList<>();

        if (carrito.getPedido() != null) {
            for (String comidaId : carrito.getPedido()) {
                comidaRepository.findById(comidaId).ifPresent(comidas::add);
            }
        }

        model.addAttribute("comidas", comidas);
        model.addAttribute("carrito", carrito);

        return "carrito";
    }

    @PostMapping("/eliminar")
    public ResponseEntity<Map<String, String>> eliminarDelCarrito(@RequestBody Map<String, String> body, HttpSession session) {
        String comidaId = body.get("comidaId");

     // Verifica si comidaId es nulo o vacío
        if (comidaId == null || comidaId.isEmpty()) {
            System.out.println("Error: ID de comida no proporcionado");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", "false", "message", "ID de comida no válido"));
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"cliente".equalsIgnoreCase(usuario.getRol())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", "false", "message", "No autorizado"));
        }

        Optional<Carrito> carritoOptional = carritoRepository.findByClienteId(usuario.getId());
        if (carritoOptional.isPresent()) {
            Carrito carrito = carritoOptional.get();
            List<String> pedidoActual = carrito.getPedido();

            if (pedidoActual != null) {
                boolean removed = pedidoActual.removeIf(item -> item.startsWith(comidaId + "|"));
                if (removed) {
                    carrito.setPedido(pedidoActual);
                    carritoRepository.save(carrito);
                    return ResponseEntity.ok(Map.of("success", "true", "message", "Producto eliminado del carrito"));
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", "false", "message", "Producto no encontrado en el carrito"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", "false", "message", "Carrito no encontrado"));
    }
}
