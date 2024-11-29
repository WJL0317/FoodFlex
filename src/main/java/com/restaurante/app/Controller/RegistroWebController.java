package com.restaurante.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registro")
public class RegistroWebController {

    @GetMapping
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Object()); // Puedes reemplazar `Object` con la entidad correspondiente
        return "registro"; // Nombre de la vista (archivo `registro.html` en `templates`)
    }
}
