package com.formacionbdi.springboot.app.productos;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.formacionbdi.springboot.app.productos.models.entity.Producto;
import com.formacionbdi.springboot.app.productos.models.service.IProductoService;

@SpringBootTest
public class ProductoControllerTest {

    @Autowired
    private IProductoService productoService;

    @Test
    public void testListar() {
        List<Producto> productos = productoService.findAll();
        assertNotNull(productos);
        assertFalse(productos.isEmpty());
        System.out.println("testListar OK - total: " + productos.size());
    }

    @Test
    public void testFindById() {
        Producto producto = productoService.findById(1L);
        assertNotNull(producto);
        assertEquals(1L, producto.getId());
        System.out.println("testFindById OK - marca: " + producto.getMarca());
    }

    @Test
    public void testCrear() {
        Producto nuevo = new Producto();
        nuevo.setMarca("TestMarca");
        nuevo.setModelo("TestModelo");
        nuevo.setAnio(2026);
        nuevo.setPrecio(500000.0);
        nuevo.setCreateAt(new Date());

        Producto guardado = productoService.save(nuevo);
        assertNotNull(guardado);
        assertNotNull(guardado.getId());
        assertEquals("TestMarca", guardado.getMarca());
        System.out.println("testCrear OK - id: " + guardado.getId());
    }

    @Test
    public void testActualizar() {
        Producto producto = productoService.findById(1L);
        assertNotNull(producto);
        String marcaOriginal = producto.getMarca();

        producto.setMarca("MarcaActualizada");
        Producto actualizado = productoService.save(producto);
        assertEquals("MarcaActualizada", actualizado.getMarca());

        // Revertir
        actualizado.setMarca(marcaOriginal);
        productoService.save(actualizado);
        System.out.println("testActualizar OK");
    }

    @Test
    public void testEliminar() {
        // Crear uno para eliminar (no tocamos los datos originales)
        Producto nuevo = new Producto();
        nuevo.setMarca("ParaEliminar");
        nuevo.setModelo("ModeloTemp");
        nuevo.setAnio(2020);
        nuevo.setPrecio(100.0);
        nuevo.setCreateAt(new Date());
        Producto guardado = productoService.save(nuevo);
        Long id = guardado.getId();

        productoService.eliminar(id);
        Producto eliminado = productoService.findById(id);
        assertNull(eliminado);
        System.out.println("testEliminar OK - id eliminado: " + id);
    }
}