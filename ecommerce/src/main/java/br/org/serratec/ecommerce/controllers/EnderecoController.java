package br.org.serratec.ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.serratec.ecommerce.dtos.EnderecoDTO;
import br.org.serratec.ecommerce.entities.Endereco;
import br.org.serratec.ecommerce.services.EnderecoService;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

	@Autowired
	EnderecoService enderecoService;

	@GetMapping
	public ResponseEntity<List<EnderecoDTO>> findAll() {
		return new ResponseEntity<>(enderecoService.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Endereco> findById(@PathVariable Long id) {
		Endereco endereco = enderecoService.findById(id);
		return new ResponseEntity<>(endereco, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<EnderecoDTO> salvar(@RequestBody Endereco endereco) {
		return new ResponseEntity<>(enderecoService.save(endereco), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<Endereco> update(@RequestBody Endereco endereco) {
		return new ResponseEntity<>(enderecoService.update(endereco), HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<String> delete(@RequestBody Endereco endereco) {
		if (enderecoService.delete(endereco)) {
			return new ResponseEntity<>("Deletado com sucesso", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Não foi possível deletar", HttpStatus.BAD_REQUEST);
		}
	}

}