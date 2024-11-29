package com.restaurante.app.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "pedidos")
public class Pedido {
    @Id
    private String id;
    private String clienteId; 
    private String nombreCliente; 
    private String restauranteId;
    private String nombreRestaurante;
    private String comidaId;
    private String nombreComida;
    private double total;
    private String estado; // PENDIENTE, EN_PROCESO, ENTREGADO, CANCELADO
    private Date fechaPedido;
    
	
    public Pedido() {
		super();
	}

	public Pedido(String id, String clienteId, String nombreCliente, String restauranteId, String nombreRestaurante,
			String comidaId, String nombreComida, double total, String estado, Date fechaPedido) {
		super();
		this.id = id;
		this.clienteId = clienteId;
		this.nombreCliente = nombreCliente;
		this.restauranteId = restauranteId;
		this.nombreRestaurante = nombreRestaurante;
		this.comidaId = comidaId;
		this.nombreComida = nombreComida;
		this.total = total;
		this.estado = estado;
		this.fechaPedido = fechaPedido;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClienteId() {
		return clienteId;
	}

	public void setClienteId(String clienteId) {
		this.clienteId = clienteId;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getRestauranteId() {
		return restauranteId;
	}

	public void setRestauranteId(String restauranteId) {
		this.restauranteId = restauranteId;
	}

	public String getNombreRestaurante() {
		return nombreRestaurante;
	}

	public void setNombreRestaurante(String nombreRestaurante) {
		this.nombreRestaurante = nombreRestaurante;
	}

	public String getComidaId() {
		return comidaId;
	}

	public void setComidaId(String comidaId) {
		this.comidaId = comidaId;
	}

	public String getNombreComida() {
		return nombreComida;
	}

	public void setNombreComida(String nombreComida) {
		this.nombreComida = nombreComida;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaPedido() {
		return fechaPedido;
	}

	public void setFechaPedido(Date fechaPedido) {
		this.fechaPedido = fechaPedido;
	}

}
