package com.residencia.ecommerce.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.residencia.ecommerce.dto.ProdutoDTO;
import com.residencia.ecommerce.entities.Produto;
import com.residencia.ecommerce.exceptions.NoSuchElementException;
import com.residencia.ecommerce.exceptions.PSQLException;
import com.residencia.ecommerce.exceptions.PropertyValueException;
import com.residencia.ecommerce.repositories.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	ProdutoRepository produtoRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	public List<ProdutoDTO> listarProdutos() {
		List<Produto> produtos = produtoRepo.findAll();
		List<ProdutoDTO> produtosDTO = new ArrayList<>();
		
		// CONVERTE ITENS DA LISTA EM DTO
		for(Produto produto : produtos) {
			produtosDTO.add(convertToDto(produto));
		}
		
		// RETORNA LISTA DE DTO
		return produtosDTO;
	}

	public Produto getProdutoPorId(Long id) {
		return produtoRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Produto", id));
	}

	public ProdutoDTO salvarProduto(String strProduto, MultipartFile arqImg) throws IOException {
		Produto produto = new Produto();
		try {
			ObjectMapper objMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);

			produto = objMapper.readValue(strProduto, Produto.class);
			Produto produtoDescricao = produtoRepo.findByDescricao(produto.getDescricao());
			if(produtoDescricao != null) {
				throw new PSQLException("Produto", "Descricao");
			}
			if(produto.getDescricao() == null || produto.getQtdEstoque() == null || produto.getValorUnitario() == null) {
				throw new PropertyValueException("Produto");
			}
		} catch (IOException e) {
			System.out.println("Erro ao converter a string produto: " + e.toString());
		}
		produto.setImagem(arqImg.getBytes());

		// SALVA O PRODUTO
		produtoRepo.save(produto);
		
		// CONVERTE PRA DTO
		ProdutoDTO produtoDTO = convertToDto(produto);
		
		// RETORNA DTO
		return produtoDTO;

	}

	public Produto atualizarProduto(Produto produto) {
		if(produto.getDescricao() == null || produto.getQtdEstoque() == null || produto.getValorUnitario() == null) {
			throw new PropertyValueException("Produto");
		}
		return produtoRepo.save(produto);
	}

	public boolean deletarProduto(Produto produto) {
		// verifica se produto é valido
		if (produto == null)
			return false;

		// verifica se existe no banco
		Produto produtoExistente = getProdutoPorId(produto.getIdProduto());
		if (produtoExistente == null)
			return false;

		// deleta produto
		produtoRepo.delete(produto);

		// verifica se foi deletado de fato
		Produto produtoContinuaExistindo = getProdutoPorId(produto.getIdProduto());
		if (produtoContinuaExistindo == null)
			return true;
		return false;
	}

	// METEDOS AUXILIARES
	
	private ProdutoDTO convertToDto(Produto produto) {
		return modelMapper.map(produto, ProdutoDTO.class);
	}

	/*
	private Produto convertToEntity(ProdutoDTO produtoDto) {
		return modelMapper.map(produtoDto, Produto.class);
	}
	*/
	
}
