package com.residencia.ecommerce.controllers;

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

import com.residencia.ecommerce.dto.EnderecoDTO;
import com.residencia.ecommerce.entities.Endereco;
import com.residencia.ecommerce.services.EnderecoService;

@RestController
@RequestMapping("/enderecos")

public class EnderecoController {

	@Autowired
	EnderecoService enderecoService;

	@GetMapping
	public ResponseEntity<List<EnderecoDTO>> listarEnderecos() {
		return new ResponseEntity<>(enderecoService.listarEnderecos(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Endereco> buscarPorId(@PathVariable Long id) {
		Endereco endereco = enderecoService.getEnderecoPorId(id);
		return new ResponseEntity<>(endereco, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<EnderecoDTO> salvar(@RequestBody Endereco endereco) {
		return new ResponseEntity<>(enderecoService.salvarEndereco(endereco), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<Endereco> atualizar(@RequestBody Endereco endereco) {
		return new ResponseEntity<>(enderecoService.atualizarEndereco(endereco), HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<String> deletarEndereco(@RequestBody Endereco endereco) {
		if (enderecoService.deletarEndereco(endereco)) {
			return new ResponseEntity<>("Deletado com sucesso", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Não foi possível deletar", HttpStatus.BAD_REQUEST);
		}
	}

}
