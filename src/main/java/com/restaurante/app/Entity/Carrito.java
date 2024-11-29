package com.restaurante.app.Entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "carritos")
public class Carrito {
    @Id
    private String id;
    private String clienteId;
    private String nombreCliente;
    private List<String> pedido; // Cambiado a lista de IDs

    // Constructor vacío
    public Carrito() {}

    // Constructor con argumentos
    public Carrito(String id, String clienteId, String nombreCliente, List<String> pedido) {
        this.id = id;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.pedido = pedido;
    }

    // Getters y Setters
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

    public List<String> getPedido() {
        return pedido;
    }

    public void setPedido(List<String> pedido) {
        this.pedido = pedido;
    }
}
