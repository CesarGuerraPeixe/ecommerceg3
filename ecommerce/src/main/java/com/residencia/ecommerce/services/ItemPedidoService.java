package com.residencia.ecommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.residencia.ecommerce.dto.ItemPedidoDTO;
import com.residencia.ecommerce.dto.RelatorioPedidoDTO;
import com.residencia.ecommerce.entities.ItemPedido;
import com.residencia.ecommerce.entities.Pedido;
import com.residencia.ecommerce.entities.Produto;
import com.residencia.ecommerce.exceptions.NoSuchElementException;
import com.residencia.ecommerce.exceptions.PropertyValueException;
import com.residencia.ecommerce.repositories.ItemPedidoRepository;
import com.residencia.ecommerce.repositories.PedidoRepository;
import com.residencia.ecommerce.repositories.ProdutoRepository;

@Service
public class ItemPedidoService {

	@Autowired
	ItemPedidoRepository itemPedidoRepo;

	@Autowired
	ProdutoRepository produtoRepo;

	@Autowired
	PedidoRepository pedidoRepo;

	@Autowired
	EmailService emailService;

	@Autowired
	PedidoService pedidoService;

	public List<ItemPedido> listarItemPedidos() {
		return itemPedidoRepo.findAll();
	}

	public ItemPedido getItemPedidoPorId(Long id) {
		return itemPedidoRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Item Pedido", id));
	}

	public ItemPedido salvarItemPedido(ItemPedido itemPedido) {

		Produto produto = produtoRepo.findById(itemPedido.getProduto().getIdProduto())
				.orElseThrow(() -> new NoSuchElementException("Produto", itemPedido.getProduto().getIdProduto()));

		if (produto != null || itemPedido.getQuantidade() != null || itemPedido.getPercentualDesconto() != null) {
			itemPedido.setPrecoVenda(produto.getValorUnitario());
			if (produto.getQtdEstoque() >= itemPedido.getQuantidade()) {
				Integer qtd = produto.getQtdEstoque() - itemPedido.getQuantidade();
				produto.setQtdEstoque(qtd);
			} else {
				System.out.println("Nao tem estoque suficiente");
				return null;
			}
		} else {
			throw new PropertyValueException("produto");
		}

		// CRIA ITEM USANDO CONSTRUTOR
		ItemPedido itemSalvo = new ItemPedido(itemPedido.getIdItemPedido(), itemPedido.getQuantidade(),
				itemPedido.getPrecoVenda(), itemPedido.getPercentualDesconto(), itemPedido.getPedido(),
				itemPedido.getProduto());

		// SALVA O ITEM
		itemPedidoRepo.save(itemSalvo);

		// ATUALIZA O VALOR TOTAL DO PEDIDO REFERENTE AO ITEM
		pedidoService.gerarValorTotal(pedidoRepo.findById(itemPedido.getPedido().getIdPedido())
				.orElseThrow(() -> new NoSuchElementException("Pedido", itemPedido.getPedido().getIdPedido())));

		// GERA RELATORIO
		RelatorioPedidoDTO pedidoDTO = gerarRelatorioDTO(pedidoRepo.findById(itemPedido.getPedido().getIdPedido())
				.orElseThrow(() -> new NoSuchElementException("Pedido", itemPedido.getPedido().getIdPedido())));

		// ENVIA EMAIL

		emailService.enviarEmail("ikaro.gaspar1@gmail.com", "Assunto entrará aqui.", pedidoDTO.toString());

		// ENVIA O ITEM SALVO COMO RESPOSTA
		return itemSalvo;
	}

	public ItemPedido atualizarItemPedido(ItemPedido itemPedido) {
		if (itemPedido.getQuantidade() == null || itemPedido.getPercentualDesconto() == null) {
			throw new PropertyValueException("Item Pedido");
		}
		// CRIA ITEM USANDO CONSTRUTOR
		ItemPedido itemSalvo = new ItemPedido(itemPedido.getIdItemPedido(), itemPedido.getQuantidade(),
				itemPedido.getPrecoVenda(), itemPedido.getPercentualDesconto(), itemPedido.getPedido(),
				itemPedido.getProduto());
		// SALVA O ITEM
		itemPedidoRepo.save(itemSalvo);

		// ATUALIZA O VALOR TOTAL DO PEDIDO REFERENTE AO ITEM
		pedidoService.gerarValorTotal(pedidoRepo.findById(itemPedido.getPedido().getIdPedido())
				.orElseThrow(() -> new NoSuchElementException("Pedido", itemPedido.getPedido().getIdPedido())));

		// GERA RELATORIO
		RelatorioPedidoDTO pedidoDTO = gerarRelatorioDTO(pedidoRepo.findById(itemPedido.getPedido().getIdPedido())
				.orElseThrow(() -> new NoSuchElementException("Pedido", itemPedido.getPedido().getIdPedido())));

		emailService.enviarEmail("ikaro.gaspar1@gmail.com", "Assunto entrará aqui.", pedidoDTO.toString());

		return itemSalvo;
	}

	public boolean deletarItemPedido(ItemPedido itemPedido) {
		// verifica se itemPedido é valido
		if (itemPedido == null)
			return false;

		// verifica se existe no banco
		ItemPedido itemPedidoExistente = getItemPedidoPorId(itemPedido.getIdItemPedido());
		if (itemPedidoExistente == null)
			return false;

		// deleta itemPedido
		itemPedidoRepo.delete(itemPedido);

		// verifica se item foi deletado de fato
		ItemPedido itemPedidoContinuaExistindo = getItemPedidoPorId(itemPedido.getIdItemPedido());
		if (itemPedidoContinuaExistindo == null) {
			// ATUALIZA O VALOR TOTAL DO PEDIDO REFERENTE AO ITEM
			pedidoService.gerarValorTotal(pedidoRepo.findById(itemPedido.getPedido().getIdPedido())
					.orElseThrow(() -> new NoSuchElementException("Pedido", itemPedido.getPedido().getIdPedido())));
			return true;
		}
		return false;
	}

	// métodos auxiliares

	private RelatorioPedidoDTO convertPedidoToDTO(Pedido pedido) {

		// CRIA LISTA RELAÇÃO DE ITENS DO PEDIDO VAZIA
		List<ItemPedidoDTO> itensPedidosDTO = new ArrayList<>();

		// VERIFICA SE PEDIDO CONTÉM ITENS
		if (pedido.getItemPedidos() != null && pedido.getItemPedidos().size() > 0) {

			// PREENCHE A RELAÇÃO DE ITENS COM OS ITENS DO PEDIDO
			for (ItemPedido itemPedido : pedido.getItemPedidos()) {

				Produto produto = produtoRepo.findById(itemPedido.getProduto().getIdProduto()).orElseThrow(
						() -> new NoSuchElementException("Produto", itemPedido.getProduto().getIdProduto()));

				// CRIA DTO ITEM PEDIDO
				ItemPedidoDTO itemPedidoDTO = new ItemPedidoDTO(itemPedido.getProduto().getIdProduto(),
						produto.getNome(), itemPedido.getPrecoVenda(), itemPedido.getQuantidade(),
						itemPedido.getValorBruto(), itemPedido.getPercentualDesconto(), itemPedido.getValorLiquido());

				// ADICIONA DTO A RELAÇÃO DE ITENS
				itensPedidosDTO.add(itemPedidoDTO);
			}
		}

		// RETORNA UM NOVO RELATORIO CONSTRUIDO A PARTIR DOS DADOS TRATADOS
		RelatorioPedidoDTO relatorioFinalizado = new RelatorioPedidoDTO(pedido.getIdPedido(), pedido.getDataPedido(),
				pedido.getValorTotal(), itensPedidosDTO);

		System.out.println(relatorioFinalizado);

		return relatorioFinalizado;
	}

	public RelatorioPedidoDTO gerarRelatorioDTO(Pedido pedido) {
		return convertPedidoToDTO(pedido);
	}

}
