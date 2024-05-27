package com.residencia.ecommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.residencia.ecommerce.dto.EnderecoDTO;
import com.residencia.ecommerce.dto.ViaCepResponse;
import com.residencia.ecommerce.entities.Endereco;
import com.residencia.ecommerce.exceptions.NoSuchElementException;
import com.residencia.ecommerce.exceptions.PropertyValueException;
import com.residencia.ecommerce.repositories.EnderecoRepository;

@Service
public class EnderecoService {

	@Autowired
	EnderecoRepository enderecoRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	private static final String VIA_CEP_URL = "https://viacep.com.br/ws/";

	public List<EnderecoDTO> listarEnderecos() {
		List<Endereco> enderecos = enderecoRepo.findAll();
		List<EnderecoDTO> enderecosDTO = new ArrayList<>();
		
		// CONVERTE ITENS DA LISTA EM DTO
		for(Endereco endereco : enderecos) {
			enderecosDTO.add(convertToDto(endereco));
		}
		
		// RETORNA LISTA DE DTO
		return enderecosDTO;
	}

	public Endereco getEnderecoPorId(Long id) {
		return enderecoRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Endereco", id));
	}

	public EnderecoDTO salvarEndereco(Endereco enderecoCep) {
		if (enderecoCep.getCep() == null) {
			throw new PropertyValueException("Endereço");
		}
		Endereco endereco = getEnderecoByCep(enderecoCep);
		
		// SALVA O PRODUTO
		enderecoRepo.save(endereco);
		
		// CONVERTE PRA DTO
		return convertToDto(endereco);
	}

	public Endereco atualizarEndereco(Endereco endereco) {
		return enderecoRepo.save(endereco);
	}

	public boolean deletarEndereco(Endereco endereco) {
		// verifica se endereco é valido
		if (endereco == null)
			return false;

		// verifica se existe no banco
		Endereco enderecoExistente = getEnderecoPorId(endereco.getIdEndereco());
		if (enderecoExistente == null)
			return false;

		// deleta endereco
		enderecoRepo.delete(endereco);

		// verifica se foi deletado de fato
		Endereco enderecoContinuaExistindo = getEnderecoPorId(endereco.getIdEndereco());
		if (enderecoContinuaExistindo == null)
			return true;
		return false;
	}

	// METEDOS AUXILIARES
	
	public Endereco getEnderecoByCep(Endereco endereco) {
		RestTemplate restTemplate = new RestTemplate();
		String viaCepUrl = VIA_CEP_URL + endereco.getCep() + "/json";
		ViaCepResponse viaCepResponse = restTemplate.getForObject(viaCepUrl, ViaCepResponse.class);
		try {
			endereco.setCep(viaCepResponse.getCep());
			endereco.setRua(viaCepResponse.getLogradouro());
			endereco.setBairro(viaCepResponse.getBairro());
			endereco.setCidade(viaCepResponse.getLocalidade());
			endereco.setComplemento(viaCepResponse.getComplemento());
			endereco.setUf(viaCepResponse.getUf());
		} catch (NullPointerException e) {
			System.out.println("O cep deve ser válido. Mensagem: " + e.getMessage());
		}

		return endereco;
	}

	private EnderecoDTO convertToDto(Endereco endereco) {
		return modelMapper.map(endereco, EnderecoDTO.class);
	}

	/*
	 * private Endereco convertToEntity(EnderecoDTO enderecoDto) { return
	 * modelMapper.map(enderecoDto, Endereco.class); }
	 */

}
