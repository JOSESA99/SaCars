package com.sacars.controller;

import com.sacars.dto.UsuarioRegistroDTO;
import com.sacars.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarFormularioLogin() {
        return "auth/login";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new UsuarioRegistroDTO());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registrarCuentaUsuario(
            @Valid @ModelAttribute("usuario") UsuarioRegistroDTO registroDTO,
            BindingResult result,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("error", "Por favor, corrija los errores del formulario");
            return "auth/registro";
        }

        if (usuarioService.existePorEmail(registroDTO.getEmail())) {
            result.rejectValue("email", "error.usuario", "Ya existe una cuenta con este email");
            model.addAttribute("error", "El correo electrónico ya está registrado");
            return "auth/registro";
        }

        try {
            usuarioService.guardar(registroDTO);
            return "redirect:/auth/login?registroExitoso";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al registrar la cuenta. Por favor, intente nuevamente.");
            return "auth/registro";
        }
    }
}
