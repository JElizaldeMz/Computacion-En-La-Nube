package com.formacionbdi.springboot.app.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Aplicacion principal del microservicio de Items.
 *
 * Notas sobre la migracion a Resilience4J:
 * - Se elimino @EnableCircuitBreaker (era de Hystrix, ya no existe)
 * - Se elimino @RibbonClient (Ribbon fue removido en Spring Cloud 2021)
 * - Resilience4J funciona automaticamente via AOP sin anotacion adicional
 * - El balanceo de carga lo hace spring-cloud-loadbalancer
 */
@EnableFeignClients
@SpringBootApplication
public class SpringbootServicioItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioItemApplication.class, args);
	}

}
