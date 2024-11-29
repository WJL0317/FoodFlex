package com.restaurante.app.Repository;

import com.restaurante.app.Entity.Pedido;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PedidoRepository extends MongoRepository<Pedido, String> {
    // Encontrar pedidos por cliente
    List<Pedido> findByClienteId(String clienteId);

    // Encontrar pedidos por restaurante
    List<Pedido> findByNombreRestaurante(String nombreRestaurante);

    // Encontrar pedidos por estado
    List<Pedido> findByEstado(String estado);

    // Encontrar pedidos por cliente y estado
    List<Pedido> findByClienteIdAndEstado(String clienteId, String estado);

    // Encontrar pedidos por restaurante y estado
    List<Pedido> findByRestauranteIdAndEstado(String restauranteId, String estado);
    
    
}
