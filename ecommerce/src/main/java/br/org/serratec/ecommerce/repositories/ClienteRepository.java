package br.org.serratec.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.org.serratec.ecommerce.entities.Cliente;
import br.org.serratec.ecommerce.entities.Endereco;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	Cliente findByCpf(String cpf);

	Cliente findByEmail(String email);

	Cliente findByEndereco(Endereco endereco);
}
