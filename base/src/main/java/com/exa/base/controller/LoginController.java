package com.exa.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.exa.base.dao.UsuarioDao;
import com.exa.base.model.Usuario;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    private UsuarioDao usuarioDao;

    @GetMapping("/login")
    public ModelAndView login(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        String mensaje = request.getParameter("Mensaje") == null ? "0" : request.getParameter("Mensaje");

        String mensajeAlert = "";
        boolean showMensaje = false;

        if (mensaje.equals("1")) {
            mensajeAlert = "Usuario registrado";
            showMensaje = true;
        } else if (mensaje.equals("2")) {
            mensajeAlert = "No se pudo registrar";
            showMensaje = true;
        }

        modelAndView.addObject("mensajeAlert", mensajeAlert);
        modelAndView.addObject("showMensaje", showMensaje);
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/registrar")
    public ModelAndView registrar() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("usuario", new Usuario());
        modelAndView.setViewName("registrar");
        return modelAndView;
    }

    @PostMapping("/registrar")
    public ModelAndView registrarUsuario(Usuario usuario) {
        String mensaje = "0";

        if (usuarioDao.grabaUsuario(usuario, new Integer[] { 1 })) {
            mensaje = "1";
        } else {
            mensaje = "2";
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/login?Mensaje=" + mensaje);
        return modelAndView;
    }

}
