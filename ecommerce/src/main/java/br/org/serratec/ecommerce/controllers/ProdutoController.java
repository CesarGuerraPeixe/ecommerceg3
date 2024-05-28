package br.org.serratec.ecommerce.controllers;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.org.serratec.ecommerce.dtos.ProdutoDTO;
import br.org.serratec.ecommerce.entities.Produto;
import br.org.serratec.ecommerce.services.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	ProdutoService produtoService;

	@GetMapping
	public ResponseEntity<List<ProdutoDTO>> findAll() {
		return new ResponseEntity<>(produtoService.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> findById(@PathVariable Long id) {
		Produto produto = produtoService.findById(id);
		return new ResponseEntity<>(produto, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ProdutoDTO> criarComFoto(@RequestPart("prod") String strProduto,
			@RequestPart("img") MultipartFile arqImg) throws IOException {
		return new ResponseEntity<>(produtoService.save(strProduto, arqImg), HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<Produto> update(@RequestBody Produto produto) {
		return new ResponseEntity<>(produtoService.update(produto), HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<String> delete(@RequestBody Produto produto) {
		if (produtoService.delete(produto)) {
			return new ResponseEntity<>("Deletado com sucesso", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Não foi possível deletar", HttpStatus.BAD_REQUEST);
		}
	}

}
