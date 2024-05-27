package com.residencia.ecommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.residencia.ecommerce.entities.Categoria;
import com.residencia.ecommerce.exceptions.NoSuchElementException;
import com.residencia.ecommerce.exceptions.PropertyValueException;
import com.residencia.ecommerce.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	CategoriaRepository categoriaRepo;

	public List<Categoria> listarCategorias() {
		return categoriaRepo.findAll();
	}

	public Categoria getCategoriaPorId(Long id) {
		return categoriaRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Categoria", id));
	}

	public Categoria salvarCategoria(Categoria categoria) {
		if (categoria.getNome() == null || categoria.getDescricao() == null) {
			throw new PropertyValueException("Categoria");
		}
		return categoriaRepo.save(categoria);
	}

	public Categoria atualizarCategoria(Categoria categoria) {
		if (categoria.getNome() == null || categoria.getDescricao() == null) {
			throw new PropertyValueException("Categoria");
		}
		return categoriaRepo.save(categoria);
	}

	public boolean deletarCategoria(Categoria categoria) {
		// verifica se categoria é valido
		if (categoria == null)
			return false;

		// verifica se existe no banco
		Categoria categoriaExistente = getCategoriaPorId(categoria.getIdCategoria());
		if (categoriaExistente == null)
			return false;

		// deleta categoria
		categoriaRepo.delete(categoria);

		// verifica se foi deletado de fato
		Categoria categoriaContinuaExistindo = getCategoriaPorId(categoria.getIdCategoria());
		if (categoriaContinuaExistindo == null)
			return true;
		return false;
	}

}
