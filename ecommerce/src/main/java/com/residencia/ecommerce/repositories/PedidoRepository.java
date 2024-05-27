package com.residencia.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.residencia.ecommerce.entities.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido,Long> {

}
