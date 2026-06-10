package com.formacionbdi.springboot.app.item.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;
import com.formacionbdi.springboot.app.item.models.service.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

/**
 * Controlador del microservicio de Items.
 *
 * Implementa el patron Circuit Breaker con Resilience4J:
 *   - @CircuitBreaker : maneja fallas al microservicio de productos
 *   - @TimeLimiter   : maneja latencia excesiva al microservicio de productos
 */
@RestController
public class ItemController {

	private static final Logger log = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	@Qualifier("serviceFeign")
	private ItemService itemService;

	// ----------------------------------------------------------------
	// GET /listar  - lista todos los items (sin circuit breaker)
	// ----------------------------------------------------------------
	@GetMapping("/listar")
	public List<Item> listar() {
		return itemService.findAll();
	}

	// ----------------------------------------------------------------
	// ESCENARIO 1: Falla en el microservicio
	// Lanza IllegalStateException para activar el Circuit Breaker.
	// Cuando el % de fallos supera failure-rate-threshold el circuito
	// se ABRE y va directo al metodoAlternativo sin llamar al servicio.
	// ----------------------------------------------------------------
	@CircuitBreaker(name = "itemsCB", fallbackMethod = "metodoAlternativo")
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		// Simula falla: lanza excepcion para activar el Circuit Breaker
		throw new IllegalStateException("Producto no encontrado!");
	}

	/**
	 * Metodo alternativo activado por @CircuitBreaker cuando el servicio falla.
	 * Registra el error en el logger y retorna datos por defecto.
	 */
	public Item metodoAlternativo(Long id, Integer cantidad, Throwable e) {
		log.error("[CircuitBreaker - FALLA] Metodo alternativo activado para id={}, cantidad={}. Causa: {}",
				id, cantidad, e.getMessage());
		Producto producto = new Producto();
		producto.setId(id);
		producto.setMarca("Sin marca (fallback)");
		producto.setModelo("Modelo no disponible - Circuit Breaker activo");
		producto.setPrecio(0.0);
		Item item = new Item();
		item.setProducto(producto);
		item.setCantidad(cantidad);
		return item;
	}

	// ----------------------------------------------------------------
	// ESCENARIO 2: Latencia excesiva en el microservicio
	// El metodo duerme 5 segundos pero el TimeLimiter tiene timeout
	// de 2s, por lo que lo corta y activa metodoAlternativoLatencia.
	// @TimeLimiter requiere retornar CompletableFuture.
	// ----------------------------------------------------------------
	@CircuitBreaker(name = "itemsTL", fallbackMethod = "metodoAlternativoLatencia")
	@TimeLimiter(name = "itemsTL")
	@GetMapping("/ver-lento/{id}/cantidad/{cantidad}")
	public CompletableFuture<Item> detalleLento(@PathVariable Long id, @PathVariable Integer cantidad) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				// Simula latencia de 5 segundos (supera el timeout de 2s del TimeLimiter)
				TimeUnit.SECONDS.sleep(5L);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			return itemService.findById(id, cantidad);
		});
	}

	/**
	 * Metodo alternativo activado por @TimeLimiter cuando el servicio supera el timeout.
	 * Registra el error en el logger y retorna datos por defecto.
	 */
	public CompletableFuture<Item> metodoAlternativoLatencia(Long id, Integer cantidad, Throwable e) {
		log.error("[TimeLimiter - LATENCIA] Metodo alternativo activado para id={}, cantidad={}. Causa: timeout superado -> {}",
				id, cantidad, e.getMessage());
		Producto producto = new Producto();
		producto.setId(id);
		producto.setMarca("Sin marca (timeout)");
		producto.setModelo("Servicio lento - TimeLimiter activo (>2s)");
		producto.setPrecio(0.0);
		Item item = new Item();
		item.setProducto(producto);
		item.setCantidad(cantidad);
		return CompletableFuture.completedFuture(item);
	}

	// ----------------------------------------------------------------
	// DELETE /eliminar/{id}
	// ----------------------------------------------------------------
	@DeleteMapping("/eliminar/{id}")
	public void eliminar(@PathVariable Long id) {
		itemService.eliminar(id);
	}
}
