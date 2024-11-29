package com.restaurante.app.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comidas")
public class Comida {
    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String restauranteNombre;
    private String categoriaNombre;   
    private String imagenUrl;     

    public Comida() {
        super();
    }

	public Comida(String id, String nombre, String descripcion, double precio, String restauranteNombre,
			String categoriaNombre, String imagenUrl) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.restauranteNombre = restauranteNombre;
		this.categoriaNombre = categoriaNombre;
		this.imagenUrl = imagenUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getRestauranteNombre() {
		return restauranteNombre;
	}

	public void setRestauranteNombre(String restauranteNombre) {
		this.restauranteNombre = restauranteNombre;
	}

	public String getCategoriaNombre() {
		return categoriaNombre;
	}

	public void setCategoriaNombre(String categoriaNombre) {
		this.categoriaNombre = categoriaNombre;
	}

	public String getImagenUrl() {
		return imagenUrl;
	}

	public void setImagenUrl(String imagenUrl) {
		this.imagenUrl = imagenUrl;
	}

}
