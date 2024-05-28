package br.org.serratec.ecommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.org.serratec.ecommerce.dtos.CepDTO;
import br.org.serratec.ecommerce.dtos.EnderecoDTO;
import br.org.serratec.ecommerce.entities.Endereco;
import br.org.serratec.ecommerce.exception.NoSuchElementException;
import br.org.serratec.ecommerce.exception.PropertyValueException;
import br.org.serratec.ecommerce.repositories.EnderecoRepository;

@Service
public class EnderecoService {

	@Autowired
	EnderecoRepository enderecoRepository;

	@Autowired
	private ModelMapper modelMapper;

	private static final String VIA_CEP_URL = "https://viacep.com.br/ws/";

	public List<EnderecoDTO> findAll() {
		List<Endereco> enderecos = enderecoRepository.findAll();
		List<EnderecoDTO> enderecosDTO = new ArrayList<>();

		// CONVERTE ITENS DA LISTA EM DTO
		for (Endereco endereco : enderecos) {
			enderecosDTO.add(convertToDto(endereco));
		}

		// RETORNA LISTA DE DTO
		return enderecosDTO;
	}

	public Endereco findById(Long id) {
		return enderecoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Endereco", id));
	}

	public EnderecoDTO save(Endereco enderecoCep) {
		if (enderecoCep.getCep() == null) {
			throw new PropertyValueException("Endereço");
		}
		Endereco endereco = getEnderecoByCep(enderecoCep);

		// SALVA O PRODUTO
		enderecoRepository.save(endereco);

		// CONVERTE PRA DTO
		return convertToDto(endereco);
	}

	public Endereco update(Endereco endereco) {
		return enderecoRepository.save(endereco);
	}

	public boolean delete(Endereco endereco) {
		// verifica se endereco é valido
		if (endereco == null)
			return false;

		// verifica se existe no banco
		Endereco enderecoExistente = findById(endereco.getIdEndereco());
		if (enderecoExistente == null)
			return false;

		// deleta endereco
		enderecoRepository.delete(endereco);

		// verifica se foi deletado de fato
		Endereco enderecoContinuaExistindo = findById(endereco.getIdEndereco());
		if (enderecoContinuaExistindo == null)
			return true;
		return false;
	}

	// METEDOS AUXILIARES

	public Endereco getEnderecoByCep(Endereco endereco) {
		RestTemplate restTemplate = new RestTemplate();
		String viaCepUrl = VIA_CEP_URL + endereco.getCep() + "/json";
		CepDTO viaCepResponse = restTemplate.getForObject(viaCepUrl, CepDTO.class);
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
