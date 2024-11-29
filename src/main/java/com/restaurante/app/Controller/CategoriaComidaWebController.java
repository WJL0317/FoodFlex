package com.restaurante.app.Controller;

import com.restaurante.app.Entity.CategoriaComida;
import com.restaurante.app.Exception.NotFoundException;
import com.restaurante.app.Repository.CategoriaComidaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categorias-comida")
public class CategoriaComidaWebController {

	@Autowired
	private CategoriaComidaRepository categoriaComidaRepository;

	// Listar todas las categorías
	@GetMapping
	public String listarCategoriasComida(Model model) {
		model.addAttribute("categorias", categoriaComidaRepository.findAll());
		return "gestionCategorias"; // Página de gestión de categorías
	}

	// Guardar una nueva categoría
	@PostMapping
	public String guardarCategoriaComida(@ModelAttribute CategoriaComida categoriaComida) {
		categoriaComidaRepository.save(categoriaComida);
		return "redirect:/categorias-comida"; // Redirige a la lista de categorías
	}

	// Eliminar una categoría existente
	@GetMapping("/eliminar/{id}")
	public String eliminarCategoria(@PathVariable String id, Model model) {
		try {
			CategoriaComida categoria = categoriaComidaRepository.findById(id)
					.orElseThrow(() -> new NotFoundException("Categoría no encontrada con id: " + id));
			categoriaComidaRepository.delete(categoria);
			model.addAttribute("mensaje", "Categoría eliminada con éxito.");
			return "redirect:/categorias-comida";
		} catch (Exception e) {
			model.addAttribute("error", "Error al eliminar la categoría: " + e.getMessage());
			return "gestionCategorias";
		}
	}

	// Mostrar el formulario de edición de una categoría
	@GetMapping("/editar/{id}")
	public String mostrarFormularioEdicion(@PathVariable String id, Model model) {
		CategoriaComida categoria = categoriaComidaRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Categoría no encontrada con id: " + id));
		model.addAttribute("categoria", categoria);
		return "editarComidaAdmin"; // Nombre de la página HTML creada
	}

	// Actualizar la categoría (ya está implementado en el controlador)
	@PostMapping("/editar")
	public String actualizarCategoria(@ModelAttribute CategoriaComida categoriaEditada, Model model) {
		try {
			CategoriaComida categoria = categoriaComidaRepository.findById(categoriaEditada.getId()).orElseThrow(
					() -> new NotFoundException("Categoría no encontrada con id: " + categoriaEditada.getId()));

			// Actualiza los campos necesarios
			categoria.setNombre(categoriaEditada.getNombre());
			categoria.setDescripcion(categoriaEditada.getDescripcion());

			categoriaComidaRepository.save(categoria);

			model.addAttribute("mensaje", "Categoría actualizada con éxito.");
			return "redirect:/categorias-comida"; // Redirige a la lista de categorías
		} catch (Exception e) {
			model.addAttribute("error", "Error al actualizar la categoría: " + e.getMessage());
			return "editarComidaAdmin"; // Devuelve al formulario en caso de error
		}
	}

}
