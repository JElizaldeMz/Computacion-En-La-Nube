package com.formacionbdi.springboot.app.productos.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.formacionbdi.springboot.app.productos.models.entity.Producto;
import com.formacionbdi.springboot.app.productos.models.service.IProductoService;

@RestController
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    // READ - Listar todos
    @GetMapping("/listar")
    public List<Producto> listar() {
        return productoService.findAll();
    }

    // READ - Ver uno
    @GetMapping("/ver/{id}")
    public ResponseEntity<Producto> detalle(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
    }

    // CREATE
    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@RequestBody Producto producto) {
        return productoService.save(producto);
    }

    // UPDATE
    @PutMapping("/editar/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto editar(@RequestBody Producto producto, @PathVariable Long id) {
        Producto productoActual = productoService.findById(id);
        productoActual.setMarca(producto.getMarca());
        productoActual.setModelo(producto.getModelo());
        productoActual.setAnio(producto.getAnio());
        productoActual.setPrecio(producto.getPrecio());
        return productoService.save(productoActual);
    }

    // DELETE
    @DeleteMapping("/eliminar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
    }
}