package com.restaurante.app.Controller;

import com.restaurante.app.Entity.Restaurante;
import com.restaurante.app.Exception.NotFoundException;
import com.restaurante.app.Repository.RestauranteRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/restaurantes")
public class RestauranteWebController {

	@Autowired
	private RestauranteRepository restauranteRepository;

	// Mostrar lista de restaurantes
	@GetMapping
	public String listarRestaurantes(Model model) {
		model.addAttribute("restaurantes", restauranteRepository.findAll());
		return "gestionRestaurantes";
	}

	@GetMapping("/inicioRestaurante")
	public String inicioRestaurante(Model model, HttpSession session) {
		Restaurante restaurante = (Restaurante) session.getAttribute("restaurante");
		if (restaurante == null) {
			return "redirect:/login"; // Redirige al login si no hay un restaurante autenticado
		}
		model.addAttribute("restaurante", restaurante);
		return "inicioRestaurante"; // Página del panel del restaurante
	}

	// Mostrar formulario para crear un nuevo restaurante
	@GetMapping("/nuevo")
	public String mostrarFormularioNuevoRestaurante(Model model) {
		model.addAttribute("restaurante", new Restaurante());
		return "restaurante/formulario"; // Vista del formulario para un nuevo restaurante
	}

	@PostMapping
	public String guardarRestaurante(@ModelAttribute Restaurante restaurante) {
		System.out.println("Datos recibidos: " + restaurante);
		restauranteRepository.save(restaurante);
		return "redirect:/login"; // Redirige al login después de registrar
	}

	@GetMapping("/editar")
	public String mostrarFormularioEditarRestaurante(HttpSession session, Model model) {
	    Restaurante restaurante = (Restaurante) session.getAttribute("restaurante");
	    if (restaurante == null) {
	        throw new NotFoundException("No hay un restaurante autenticado.");
	    }
	    model.addAttribute("restaurante", restaurante);
	    return "editarRestaurante"; // Vista de edición del restaurante
	}

	@PostMapping("/editar")
	public String actualizarRestaurante(@ModelAttribute Restaurante restauranteActualizado, HttpSession session, Model model) {
	    Restaurante restaurante = restauranteRepository.findById(restauranteActualizado.getId())
	            .orElseThrow(() -> new NotFoundException("Restaurante no encontrado con id: " + restauranteActualizado.getId()));

	    restaurante.setNombre(restauranteActualizado.getNombre());
	    restaurante.setDireccion(restauranteActualizado.getDireccion());
	    restaurante.setTelefono(restauranteActualizado.getTelefono());
	    restaurante.setEmail(restauranteActualizado.getEmail());
	    restaurante.setDescripcion(restauranteActualizado.getDescripcion());

	    restauranteRepository.save(restaurante);

	    // Actualiza la sesión con los datos actualizados
	    session.setAttribute("restaurante", restaurante);

	    return "redirect:/login/inicioRestaurante";
	}
	
	

	@GetMapping("/eliminar/{id}")
	public String eliminarRestaurante(@PathVariable String id, Model model) {
	    try {
	        Restaurante restaurante = restauranteRepository.findById(id)
	                .orElseThrow(() -> new NotFoundException("Restaurante no encontrado con id: " + id));

	        restauranteRepository.delete(restaurante);

	        model.addAttribute("mensaje", "El restaurante fue eliminado con éxito.");
	    } catch (NotFoundException e) {
	        model.addAttribute("error", e.getMessage());
	    } catch (Exception e) {
	        model.addAttribute("error", "No se pudo eliminar el restaurante. Intenta nuevamente.");
	    }
	    return "redirect:/restaurantes";
	}

	@GetMapping("/detallePorNombre/{nombre}")
	@ResponseBody
	public Restaurante obtenerDetallePorNombre(@PathVariable("nombre") String nombre) {
	    System.out.println("Nombre recibido: " + nombre); // Depuración
	    return restauranteRepository.findByNombreIgnoreCase(nombre)
	            .orElseThrow(() -> new NotFoundException("Restaurante no encontrado con nombre: " + nombre));
	}
	
	@GetMapping("/admin/editar/{id}")
	public String mostrarFormularioEditarRestauranteAdmin(@PathVariable String id, Model model) {
	    Restaurante restaurante = restauranteRepository.findById(id)
	            .orElseThrow(() -> new NotFoundException("Restaurante no encontrado con id: " + id));
	    model.addAttribute("restaurante", restaurante);
	    return "editarRestaurantesAdmin"; // Nombre de la página creada
	}

	@PostMapping("/admin/RestauranteAdmin/editar")
	public String actualizarRestauranteAdmin(@ModelAttribute Restaurante restauranteActualizado, Model model) {
	    Restaurante restaurante = restauranteRepository.findById(restauranteActualizado.getId())
	            .orElseThrow(() -> new NotFoundException("Restaurante no encontrado con id: " + restauranteActualizado.getId()));

	    // Actualiza los campos necesarios
	    restaurante.setNombre(restauranteActualizado.getNombre());
	    restaurante.setDireccion(restauranteActualizado.getDireccion());
	    restaurante.setTelefono(restauranteActualizado.getTelefono());
	    restaurante.setEmail(restauranteActualizado.getEmail());
	    restaurante.setDescripcion(restauranteActualizado.getDescripcion());

	    restauranteRepository.save(restaurante);

	    model.addAttribute("mensaje", "Restaurante actualizado con éxito.");
	    return "redirect:/restaurantes"; // Redirige a la lista de restaurantes
	}

}
