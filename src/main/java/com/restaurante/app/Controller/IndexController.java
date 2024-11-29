package com.restaurante.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.restaurante.app.Repository.ComidaRepository;


@Controller
public class IndexController {

    @Autowired
    private ComidaRepository comidaRepository;

    // Página principal con el catálogo de comidas
    @GetMapping("/")
    public String getAllComidas(Model model) {
        model.addAttribute("comidas", comidaRepository.findAll());
        return "index";
    }

    // Página de categoría
    @GetMapping("/categorias")
    public String categorias() {
        return "categorias";
    }

    // Página "Acerca de"
    @GetMapping("/acerca")
    public String acerca() {
        return "acerca";
    }

    // Página "Nosotros"
    @GetMapping("/nosotros")
    public String nosotros() {
        return "nosotros";
    }
}
