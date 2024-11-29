package com.restaurante.app.Controller;

import com.restaurante.app.Entity.Usuario;
import com.restaurante.app.Entity.Restaurante;
import com.restaurante.app.Exception.NotFoundException;
import com.restaurante.app.Repository.UsuarioRepository;
import com.restaurante.app.Repository.RestauranteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registro")
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @PostMapping("/usuario")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario) {
        // Validar y procesar el rol
        String rol = usuario.getRol() != null ? usuario.getRol().trim().toUpperCase() : "";

        if (!"CLIENTE".equals(rol) && !"ADMIN".equals(rol)) {
            throw new IllegalArgumentException("Rol inválido. Solo se permiten CLIENTE o ADMIN.");
        }

        usuario.setRol(rol);
        usuarioRepository.save(usuario); // Guarda el usuario en la base de datos

        // Mensaje personalizado basado en el rol
        String mensaje = "CLIENTE".equals(rol) 
                ? "Cliente registrado correctamente" 
                : "Administrador registrado correctamente";

        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }


    // Registrar un restaurante
    @PostMapping("/restaurante")
    public ResponseEntity<Restaurante> registrarRestaurante(@RequestBody Restaurante restaurante) {
        // Verificar si ya existe un restaurante con el mismo email o nombre de usuario
        boolean existeEmail = restauranteRepository.findAll().stream()
                .anyMatch(r -> r.getEmail().equalsIgnoreCase(restaurante.getEmail()));
        boolean existeUsuario = restauranteRepository.findAll().stream()
                .anyMatch(r -> r.getUsuario().equalsIgnoreCase(restaurante.getUsuario()));

        if (existeEmail || existeUsuario) {
            throw new IllegalArgumentException("El correo o usuario ya está en uso.");
        }

        Restaurante nuevoRestaurante = restauranteRepository.save(restaurante);
        return new ResponseEntity<>(nuevoRestaurante, HttpStatus.CREATED);
    }

    // Obtener todos los usuarios registrados
    @GetMapping("/usuarios")
    public ResponseEntity<Iterable<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    // Obtener todos los restaurantes registrados
    @GetMapping("/restaurantes")
    public ResponseEntity<Iterable<Restaurante>> listarRestaurantes() {
        return ResponseEntity.ok(restauranteRepository.findAll());
    }
}
