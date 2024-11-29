package com.restaurante.app.Controller;

import com.restaurante.app.Entity.Restaurante;
import com.restaurante.app.Entity.Usuario;
import com.restaurante.app.Repository.ComidaRepository;
import com.restaurante.app.Repository.UsuarioRepository;
import com.restaurante.app.Repository.RestauranteRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComidaRepository comidaRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    // Muestra la página de login
    @GetMapping
    public String mostrarLoginForm() {
        return "login"; // Página login.html
    }

    // Maneja la autenticación de usuarios
    @PostMapping("/usuario")
    public String loginUsuario(@RequestParam String usuario, @RequestParam String contrasena, Model model,
                                HttpSession session) {
        // Buscar en la tabla de usuarios
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findAll().stream()
                .filter(u -> u.getUsuario() != null && u.getContrasena() != null) // Evita valores nulos
                .filter(u -> u.getUsuario().equalsIgnoreCase(usuario) && u.getContrasena().equals(contrasena))
                .findFirst();

        if (usuarioEncontrado.isPresent()) {
            Usuario usuarioAutenticado = usuarioEncontrado.get();
            session.setAttribute("usuario", usuarioAutenticado);

            switch (usuarioAutenticado.getRol().trim().toLowerCase()) { // Elimina espacios y compara en minúsculas
                case "admin":
                    return "redirect:/login/inicioAdmin";
                case "cliente":
                    return "redirect:/login/inicioCliente";
                default:
                    model.addAttribute("error", "Rol desconocido.");
                    return "login";
            }
        }

        // Buscar en la tabla de restaurantes
        Optional<Restaurante> restauranteEncontrado = restauranteRepository.findAll().stream()
                .filter(r -> r.getUsuario() != null && r.getContrasena() != null) // Evita valores nulos
                .filter(r -> r.getUsuario().equalsIgnoreCase(usuario) && r.getContrasena().equals(contrasena))
                .findFirst();

        if (restauranteEncontrado.isPresent()) {
            Restaurante restauranteAutenticado = restauranteEncontrado.get();
            session.setAttribute("restaurante", restauranteAutenticado);

            // Logs de depuración
            System.out.println("Restaurante autenticado: " + restauranteAutenticado.getNombre());
            System.out.println("ID Restaurante: " + restauranteAutenticado.getId());

            return "redirect:/login/inicioRestaurante"; // Redirige al panel de restaurante
        }

        // Si las credenciales no coinciden
        model.addAttribute("error", "Credenciales incorrectas, intente de nuevo.");
        return "login"; // Retorna al formulario con error
    }

    // Página de inicio del cliente con el catálogo de comidas
    @GetMapping("/inicioCliente")
    public String inicioCliente(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Validar que el usuario esté autenticado y sea un cliente
        if (usuario == null || !"cliente".equalsIgnoreCase(usuario.getRol())) {
            return "redirect:/login"; // Redirige al login si no hay un cliente autenticado
        }

        // Log de depuración
        System.out.println("Cliente autenticado: " + usuario.getUsuario());

        // Agregar datos del cliente autenticado al modelo
        model.addAttribute("usuario", usuario);

        // Agregar la lista de comidas al modelo
        model.addAttribute("comidas", comidaRepository.findAll());

        return "inicioCliente"; // Página inicioCliente.html
    }

    // Página de inicio del administrador
    @GetMapping("/inicioAdmin")
    public String inicioAdmin(Model model, HttpSession session) {
        Usuario administrador = (Usuario) session.getAttribute("usuario");

        // Validar que el usuario esté autenticado y sea administrador
        if (administrador == null || !"admin".equalsIgnoreCase(administrador.getRol())) {
            return "redirect:/login"; // Redirigir a login si no es administrador
        }

        // Log de depuración
        System.out.println("Administrador autenticado: " + administrador.getUsuario());

        model.addAttribute("administrador", administrador);
        return "inicioAdmin";
    }

    // Página de inicio del restaurante
    @GetMapping("/inicioRestaurante")
    public String inicioRestaurante(Model model, HttpSession session) {
        Restaurante restaurante = (Restaurante) session.getAttribute("restaurante");

        // Validar que el restaurante esté autenticado
        if (restaurante == null) {
            System.out.println("Error: No se encontró un restaurante en la sesión.");
            return "redirect:/login"; // Redirigir al login si no hay un restaurante autenticado
        }

        // Log de depuración
        System.out.println("Restaurante autenticado: " + restaurante.getNombre() + " (ID: " + restaurante.getId() + ")");

        model.addAttribute("restaurante", restaurante);
        return "inicioRestaurante"; // Página inicioRestaurante.html
    }

    // Método para cerrar sesión
    @GetMapping("/cerrarSesion")
    public String cerrarSesion(HttpSession session) {
        System.out.println("Sesión cerrada.");
        session.invalidate(); // Invalida la sesión actual
        return "redirect:/"; // Redirige a la página principal (index)
    }
}
