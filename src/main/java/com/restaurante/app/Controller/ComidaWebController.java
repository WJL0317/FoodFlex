package com.restaurante.app.Controller;

import com.restaurante.app.Entity.Comida;
import com.restaurante.app.Exception.NotFoundException;
import com.restaurante.app.Repository.ComidaRepository;
import com.restaurante.app.Repository.CategoriaComidaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comidas")
public class ComidaWebController {

	@Autowired
	private ComidaRepository comidaRepository;
	@Autowired
	private CategoriaComidaRepository categoriaComidaRepository;

	// Mostrar lista de comidas
	@GetMapping
	public String listarComidas(Model model) {
		model.addAttribute("comidas", comidaRepository.findAll());
		return "gestionComidas"; // Vista para listar comidas
	}
	
	@GetMapping("/nuevo")
	public String mostrarFormularioNuevaComida(Model model) {
		model.addAttribute("comida", new Comida());
		// Cargar las categorías desde el repositorio
		model.addAttribute("categorias", categoriaComidaRepository.findAll());
		return "registroComida"; // Vista del formulario para registrar una nueva comida
	}

	// Guardar una nueva comida
	@PostMapping("/guardar")
	public String guardarComida(@ModelAttribute Comida comida, Model model) {
		try {
			comidaRepository.save(comida); // Guarda la comida en la base de datos
			model.addAttribute("success", "Comida registrada exitosamente");
			return "redirect:/comidas"; // Redirige a la lista de comidas
		} catch (Exception e) {
			model.addAttribute("error", "Error al registrar la comida: " + e.getMessage());
			return "registroComida"; // Retorna al formulario en caso de error
		}
	}

	// Mostrar formulario para editar una comida existente
	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditarComida(@PathVariable String id, Model model) {
		Comida comida = comidaRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Comida no encontrada con id: " + id));
		model.addAttribute("comida", comida);
		return "editarComida"; // Vista para editar comidas
	}

	// Actualizar una comida existente
	@PostMapping("/editar/{id}")
	public String actualizarComida(@PathVariable String id, @ModelAttribute Comida comidaActualizada, Model model) {
	    try {
	        Comida comida = comidaRepository.findById(id)
	                .orElseThrow(() -> new NotFoundException("Comida no encontrada con id: " + id));

	        // Actualiza los campos de la comida
	        comida.setNombre(comidaActualizada.getNombre());
	        comida.setDescripcion(comidaActualizada.getDescripcion());
	        comida.setPrecio(comidaActualizada.getPrecio());
	        comida.setRestauranteNombre(comidaActualizada.getRestauranteNombre());
	        comida.setCategoriaNombre(comidaActualizada.getCategoriaNombre());
	        comida.setImagenUrl(comidaActualizada.getImagenUrl());

	        comidaRepository.save(comida); // Guarda los cambios
	        model.addAttribute("success", "Comida actualizada exitosamente");
	        return "redirect:/comidas"; // Redirige a la lista de comidas
	    } catch (Exception e) {
	        model.addAttribute("error", "Error al actualizar la comida: " + e.getMessage());
	        return "editarComida"; // Retorna al formulario en caso de error
	    }
	}


	// Eliminar una comida
	@GetMapping("/eliminar/{id}")
	public String eliminarComida(@PathVariable String id, Model model) {
		try {
			Comida comida = comidaRepository.findById(id)
					.orElseThrow(() -> new NotFoundException("Comida no encontrada con id: " + id));

			comidaRepository.delete(comida); // Elimina la comida
			model.addAttribute("success", "Comida eliminada exitosamente");
			return "redirect:/comidas"; // Redirige a la lista de comidas
		} catch (Exception e) {
			model.addAttribute("error", "Error al eliminar la comida: " + e.getMessage());
			return "gestionComidas"; // Retorna a la lista en caso de error
		}
	}
}
