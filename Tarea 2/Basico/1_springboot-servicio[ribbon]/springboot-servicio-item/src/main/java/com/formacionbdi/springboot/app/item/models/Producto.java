package com.formacionbdi.springboot.app.item.models;

import java.util.Date;

public class Producto {
	
	private Long id;
	private String marca;
	private String modelo;
	private Integer anio;
	private Double precio;
	private Date createAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getModelo(){
		return modelo;
	}
	public void setModelo(String modelo){
		this.modelo = modelo;
	}
	public Integer getAnio(){
		return anio;
	}
	public void setAnio(Integer anio){
		this.anio = anio;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	

}
