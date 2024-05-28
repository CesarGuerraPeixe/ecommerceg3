package br.org.serratec.ecommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.serratec.ecommerce.dtos.ClienteDTO;
import br.org.serratec.ecommerce.entities.Cliente;
import br.org.serratec.ecommerce.entities.Endereco;
import br.org.serratec.ecommerce.exception.NoSuchElementException;
import br.org.serratec.ecommerce.exception.PSQLException;
import br.org.serratec.ecommerce.exception.PropertyValueException;
import br.org.serratec.ecommerce.repositories.ClienteRepository;
import br.org.serratec.ecommerce.repositories.EnderecoRepository;

@Service
public class ClienteService {

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	EnderecoRepository enderecoRepository;

	@Autowired
	ModelMapper modelMapper;

	public List<ClienteDTO> findAll() {
		// CRIA LISTA DE CLIENTES E CLIENTES DTO.
		List<Cliente> clientes = clienteRepository.findAll();
		List<ClienteDTO> clientesDTO = new ArrayList<>();

		// CONVERTE ITENS DA LISTA EM DTO
		for (Cliente cliente : clientes) {
			clientesDTO.add(convertToDto(cliente));
		}

		// RETORNA LISTA DE DTO
		return clientesDTO;
	}

	public Cliente findById(Long id) {
		return clienteRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Cliente", id));
	}

	public ClienteDTO save(Cliente cliente) {

		Endereco endereco = enderecoRepository.findById(cliente.getEndereco().getIdEndereco())
				.orElseThrow(() -> new NoSuchElementException("Endereco", cliente.getEndereco().getIdEndereco()));

		Cliente clienteCpf = clienteRepository.findByCpf(cliente.getCpf());
		Cliente clienteEmail = clienteRepository.findByEmail(cliente.getEmail());
		Cliente clienteEndereco = clienteRepository.findByEndereco(endereco);

		// VERIFICAÇÃO DOS CAMPOS JÁ EXISTENTES NO BANCO DE DADOS
		if (clienteCpf != null) {
			throw new PSQLException("Cliente", "CPF");
		} else if (clienteEmail != null) {
			throw new PSQLException("Cliente", "Email");
		} else if (clienteEndereco != null) {
			throw new PSQLException("Cliente", "Endereco");
		}

		// VERIFICA SE OS CAMPOS ESTÃO NULOS NA REQUISIÇÃO
		if (cliente.getEmail() == null || cliente.getCpf() == null || cliente.getEndereco() == null) {
			throw new PropertyValueException("Cliente");
		}

		// SALVA O PRODUTO
		clienteRepository.save(cliente);

		// CONVERTE PRA DTO
		ClienteDTO clienteDTO = convertToDto(cliente);

		// RETORNA DTO
		return clienteDTO;
	}

	public Cliente update(Cliente cliente) {
		if (cliente.getEmail() == null || cliente.getCpf() == null || cliente.getEndereco() == null) {
			throw new PropertyValueException("Cliente");
		}
		return clienteRepository.save(cliente);
	}

	public boolean delete(Cliente cliente) {

		if (cliente == null)
			return false;

		Cliente clienteExistente = findById(cliente.getIdCliente());
		if (clienteExistente == null)
			return false;

		clienteRepository.delete(cliente);

		Cliente clienteContinuaExistindo = findById(cliente.getIdCliente());
		if (clienteContinuaExistindo == null)
			return true;
		return false;
	}

	// METEDOS AUXILIARES

	private ClienteDTO convertToDto(Cliente cliente) {
		return modelMapper.map(cliente, ClienteDTO.class);
	}

	/*
	 * private Cliente convertToEntity(ClienteDTO clienteDto) { return
	 * modelMapper.map(clienteDto, Cliente.class); }
	 */

}
