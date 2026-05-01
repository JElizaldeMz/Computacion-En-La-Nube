package com.formacionbdi.springboot.app.item.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.service.ItemService;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.formacionbdi.springboot.app.item.models.Producto;

@RestController
public class ItemController {

	
	@Autowired
//	@Qualifier("serviceRestTemplate")
	@Qualifier("serviceFeign")
	private ItemService itemService;
	
	@GetMapping("/listar")
	public List<Item> listar(){
		return itemService.findAll();
	}
	

	@HystrixCommand(fallbackMethod = "metodoAlternativo")
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		return itemService.findById(id, cantidad);
	}

		public Item metodoAlternativo(Long id, Integer cantidad) {
		Producto producto = new Producto();
		producto.setId(id);
		producto.setMarca("Sin marca");
		producto.setModelo("Modelo no disponible");
		producto.setPrecio(0.0);
		Item item = new Item();
		item.setProducto(producto);
		item.setCantidad(cantidad);
		return item;
	}

	// -------------------------------------------------------
	// Latencia: simula un microservicio lento (2 segundos).
	// Hystrix tiene timeout de 1 segundo -> activa fallback.
	// -------------------------------------------------------
	@HystrixCommand(fallbackMethod = "metodoAlternativoLatencia")
	@GetMapping("/ver-lento/{id}/cantidad/{cantidad}")
	public Item detalleLento(@PathVariable Long id, @PathVariable Integer cantidad) {
		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return itemService.findById(id, cantidad);
	}

	public Item metodoAlternativoLatencia(Long id, Integer cantidad) {
		Producto producto = new Producto();
		producto.setId(id);
		producto.setMarca("Sin marca - timeout superado");
		producto.setModelo("Servicio lento, fallback activado");
		producto.setPrecio(0.0);
		Item item = new Item();
		item.setProducto(producto);
		item.setCantidad(cantidad);
		return item;
	}

}
