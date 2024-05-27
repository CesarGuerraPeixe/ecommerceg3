package com.residencia.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.residencia.ecommerce.entities.Produto;

public interface ProdutoRepository extends JpaRepository<Produto,Long>{

	Produto findByDescricao(String descricao);
}
