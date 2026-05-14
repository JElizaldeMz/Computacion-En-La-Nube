package com.formacionbdi.springboot.app.productos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.formacionbdi.springboot.app.productos.models.service.IProductoService;

@Controller
public class ModeloViewController {

    @Autowired
    private IProductoService productoService;

    @GetMapping("/modelos")
    public String listarModelos(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "modelos";
    }
}