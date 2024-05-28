package br.org.serratec.ecommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.serratec.ecommerce.entities.Categoria;
import br.org.serratec.ecommerce.exception.NoSuchElementException;
import br.org.serratec.ecommerce.exception.PropertyValueException;
import br.org.serratec.ecommerce.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	CategoriaRepository categoriaRepository;

	public List<Categoria> findAll() {
		return categoriaRepository.findAll();
	}

	public Categoria findById(Long id) {
		return categoriaRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Categoria", id));
	}

	public Categoria save(Categoria categoria) {
		if (categoria.getNome() == null || categoria.getDescricao() == null) {
			throw new PropertyValueException("Categoria");
		}
		return categoriaRepository.save(categoria);
	}

	public Categoria update(Categoria categoria) {
		if (categoria.getNome() == null || categoria.getDescricao() == null) {
			throw new PropertyValueException("Categoria");
		}
		return categoriaRepository.save(categoria);
	}

	public boolean delete(Categoria categoria) {
		// verifica se categoria é valido
		if (categoria == null)
			return false;

		// verifica se existe no banco
		Categoria categoriaExistente = findById(categoria.getIdCategoria());
		if (categoriaExistente == null)
			return false;

		// deleta categoria
		categoriaRepository.delete(categoria);

		// verifica se foi deletado de fato
		Categoria categoriaContinuaExistindo = findById(categoria.getIdCategoria());
		if (categoriaContinuaExistindo == null)
			return true;
		return false;
	}

}