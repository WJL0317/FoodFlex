package com.restaurante.app.Controller;

import com.restaurante.app.Entity.Usuario;
import com.restaurante.app.Exception.NotFoundException;
import com.restaurante.app.Repository.UsuarioRepository;


import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioWebController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Mostrar formulario para crear un nuevo usuario
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registroAdmin"; // Vista existente para registrar un administrador
    }

    // Guardar un nuevo usuario
    @PostMapping
    public String guardarUsuario(@ModelAttribute Usuario usuario, Model model) {
        try {
            usuario.setRol("Admin"); // Asegura que el rol se configure correctamente
            usuarioRepository.save(usuario); // Guarda en la base de datos
            model.addAttribute("success", "Administrador creado correctamente");
            return "redirect:/login/inicioAdmin"; // Redirige después de guardar
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar el administrador: " + e.getMessage());
            return "registroAdmin"; // Retorna a la página de registro con el error
        }
    }

    // Actualizar un usuario existente
    @PostMapping("/editar/{id}")
    public String actualizarUsuario(@PathVariable String id, @ModelAttribute Usuario usuarioActualizado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id: " + id));

        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setRol(usuarioActualizado.getRol());
        usuario.setUsuario(usuarioActualizado.getUsuario());
        usuario.setContrasena(usuarioActualizado.getContrasena());

        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }

    // Eliminar un usuario
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id: " + id));

        usuarioRepository.delete(usuario);
        return "redirect:/usuarios";
    }
    
    @GetMapping("/editarAdmin")
    public String mostrarFormularioEditarAdmin(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            System.out.println("Usuario no encontrado en la sesión.");
            return "redirect:/login"; // Redirige si no hay usuario en sesión
        }

        System.out.println("Usuario encontrado: " + usuario.getNombre());
        model.addAttribute("usuario", usuario); // Precarga los datos del administrador
        return "editarAdmin"; // Página de edición
    }


    // Procesar la edición del administrador
    @PostMapping("/editarAdmin")
    public String editarAdmin(
            @ModelAttribute Usuario usuarioEditado,
            HttpSession session,
            Model model) {

        try {
            // Recuperar al usuario actual desde la sesión
            Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
            if (usuarioActual == null) {
                return "redirect:/login"; // Redirige si la sesión ha expirado
            }

            // Actualizar los datos del administrador en la base de datos
            usuarioActual.setNombre(usuarioEditado.getNombre());
            usuarioActual.setEmail(usuarioEditado.getEmail());
            usuarioActual.setTelefono(usuarioEditado.getTelefono());
            usuarioActual.setUsuario(usuarioEditado.getUsuario());
            usuarioActual.setContrasena(usuarioEditado.getContrasena());

            usuarioRepository.save(usuarioActual); // Guarda los cambios

            // Actualizar la sesión con los datos modificados
            session.setAttribute("usuario", usuarioActual);

            model.addAttribute("success", "Perfil actualizado correctamente");
            return "redirect:/login/inicioAdmin"; // Redirige al panel del administrador
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar el perfil: " + e.getMessage());
            return "editarAdmin"; // Retorna al formulario en caso de error
        }
    }
    
 // ====================
    // CLIENTES
    // ====================

    // Listar todos los clientes
    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        List<Usuario> clientes = usuarioRepository.findAllByRol("Cliente");
        System.out.println("Clientes encontrados: " + clientes); // Depuración
        model.addAttribute("clientes", clientes);
        return "gestionClientes";
    }

    // Mostrar formulario para registrar un nuevo cliente
    @GetMapping("/nuevoCliente")
    public String mostrarFormularioNuevoCliente(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registroCliente"; // Vista para registrar clientes
    }

    // Guardar un nuevo cliente
    @PostMapping("/guardarCliente")
    public String guardarCliente(@ModelAttribute Usuario usuario, Model model) {
        try {
            usuario.setRol("Cliente"); // Configura el rol como cliente
            usuarioRepository.save(usuario); // Guarda en la base de datos
            model.addAttribute("success", "Cliente creado correctamente");
            return "redirect:/usuarios/clientes"; // Redirige a la lista de clientes
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar el cliente: " + e.getMessage());
            return "registroCliente"; // Retorna a la página de registro con el error
        }
    }

    // Editar cliente
    @GetMapping("/editarCliente/{id}")
    public String mostrarFormularioEditarCliente(@PathVariable String id, Model model) {
        Usuario cliente = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con id: " + id));
        model.addAttribute("usuario", cliente);
        return "editarCliente"; // Vista para editar clientes
    }

    @PostMapping("/editarCliente/{id}")
    public String actualizarCliente(@PathVariable String id, @ModelAttribute Usuario usuarioActualizado) {
        Usuario cliente = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con id: " + id));

        cliente.setNombre(usuarioActualizado.getNombre());
        cliente.setEmail(usuarioActualizado.getEmail());
        cliente.setTelefono(usuarioActualizado.getTelefono());
        cliente.setUsuario(usuarioActualizado.getUsuario());
        cliente.setContrasena(usuarioActualizado.getContrasena());

        usuarioRepository.save(cliente);
        return "redirect:/usuarios/clientes";
    }

    // Eliminar cliente
    @GetMapping("/eliminarCliente/{id}")
    public String eliminarCliente(@PathVariable String id) {
        Usuario cliente = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con id: " + id));
        usuarioRepository.delete(cliente);
        return "redirect:/usuarios/clientes";
    }
    
 // Mostrar formulario para que el cliente edite su información personal
 // Mostrar formulario para que el cliente edite su información personal
    @GetMapping("/editarPerfil")
    public String mostrarFormularioEditarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"cliente".equalsIgnoreCase(usuario.getRol())) {
            throw new NotFoundException("No hay un cliente autenticado.");
        }
        model.addAttribute("usuario", usuario); // Cargar datos del cliente autenticado
        return "editarInfoCliente"; // Página para editar la información del cliente
    }


    // Procesar la edición de la información del cliente
    @PostMapping("/editarPerfil")
    public String editarPerfilCliente(@ModelAttribute Usuario usuarioActualizado, HttpSession session, Model model) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario == null || !"cliente".equalsIgnoreCase(usuario.getRol())) {
                throw new NotFoundException("No hay un cliente autenticado.");
            }

            // Actualizar los datos en la base de datos
            usuario.setNombre(usuarioActualizado.getNombre());
            usuario.setEmail(usuarioActualizado.getEmail());
            usuario.setTelefono(usuarioActualizado.getTelefono());
            usuario.setUsuario(usuarioActualizado.getUsuario());
            usuario.setContrasena(usuarioActualizado.getContrasena());

            usuarioRepository.save(usuario);
            session.setAttribute("usuario", usuario); // Actualizar la sesión con los cambios
            model.addAttribute("success", "Información actualizada correctamente");
            return "redirect:/login/inicioCliente"; // Redirigir al panel del cliente
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar la información: " + e.getMessage());
            return "editarInfoCliente"; // Retornar al formulario en caso de error
        }
    }
    
    @GetMapping("/inicioCliente")
    public String mostrarInicioCliente(@RequestParam("clienteId") String clienteId, Model model) {
        model.addAttribute("clienteId", clienteId); // Asegura que el clienteId se pase al modelo
        return "inicioCliente"; // Vista que contiene la sección del panel de cliente
    }
}


