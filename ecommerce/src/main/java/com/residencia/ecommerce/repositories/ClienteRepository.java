package com.residencia.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.residencia.ecommerce.entities.Cliente;
import com.residencia.ecommerce.entities.Endereco;

public interface ClienteRepository extends JpaRepository<Cliente,Long> {
	
	Cliente findByCpf(String cpf);
	Cliente findByEmail(String email);
	Cliente findByEndereco(Endereco endereco);
}
