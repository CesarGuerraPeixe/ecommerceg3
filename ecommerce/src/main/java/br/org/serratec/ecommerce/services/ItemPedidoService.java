package br.org.serratec.ecommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.serratec.ecommerce.dtos.ItemPedidoDTO;
import br.org.serratec.ecommerce.dtos.RelatorioPedidoDTO;
import br.org.serratec.ecommerce.entities.ItemPedido;
import br.org.serratec.ecommerce.entities.Pedido;
import br.org.serratec.ecommerce.entities.Produto;
import br.org.serratec.ecommerce.exception.NoSuchElementException;
import br.org.serratec.ecommerce.exception.PropertyValueException;
import br.org.serratec.ecommerce.repositories.ItemPedidoRepository;
import br.org.serratec.ecommerce.repositories.PedidoRepository;
import br.org.serratec.ecommerce.repositories.ProdutoRepository;

@Service
public class ItemPedidoService {

	@Autowired
	ItemPedidoRepository itemPedidoRepository;

	@Autowired
	ProdutoRepository produtoRepository;

	@Autowired
	PedidoRepository pedidoRepository;

	@Autowired
	EmailService emailService;

	@Autowired
	PedidoService pedidoService;

	public List<ItemPedido> findAll() {
		return itemPedidoRepository.findAll();
	}

	public ItemPedido findById(Long id) {
		return itemPedidoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Item Pedido", id));
	}

	public ItemPedido save(ItemPedido itemPedido) {

		Produto produto = produtoRepository.findById(itemPedido.getProduto().getIdProduto())
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
		itemPedidoRepository.save(itemSalvo);

		// ATUALIZA O VALOR TOTAL DO PEDIDO REFERENTE AO ITEM
		pedidoService.gerarValorTotal(pedidoRepository.findById(itemPedido.getPedido().getIdPedido())
				.orElseThrow(() -> new NoSuchElementException("Pedido", itemPedido.getPedido().getIdPedido())));

		// GERA RELATORIO
		RelatorioPedidoDTO pedidoDTO = gerarRelatorioDTO(pedidoRepository.findById(itemPedido.getPedido().getIdPedido())
				.orElseThrow(() -> new NoSuchElementException("Pedido", itemPedido.getPedido().getIdPedido())));

		// ENVIA EMAIL

		emailService.enviarEmail("cesargpmuller@gmail.com", "Assunto entrará aqui.", pedidoDTO.toString());

		// ENVIA O ITEM SALVO COMO RESPOSTA
		return itemSalvo;
	}

	public ItemPedido update(ItemPedido itemPedido) {
		if (itemPedido.getQuantidade() == null || itemPedido.getPercentualDesconto() == null) {
			throw new PropertyValueException("Item Pedido");
		}
		// CRIA ITEM USANDO CONSTRUTOR
		ItemPedido itemSalvo = new ItemPedido(itemPedido.getIdItemPedido(), itemPedido.getQuantidade(),
				itemPedido.getPrecoVenda(), itemPedido.getPercentualDesconto(), itemPedido.getPedido(),
				itemPedido.getProduto());
		// SALVA O ITEM
		itemPedidoRepository.save(itemSalvo);

		// ATUALIZA O VALOR TOTAL DO PEDIDO REFERENTE AO ITEM
		pedidoService.gerarValorTotal(pedidoRepository.findById(itemPedido.getPedido().getIdPedido())
				.orElseThrow(() -> new NoSuchElementException("Pedido", itemPedido.getPedido().getIdPedido())));

		// GERA RELATORIO
		RelatorioPedidoDTO pedidoDTO = gerarRelatorioDTO(pedidoRepository.findById(itemPedido.getPedido().getIdPedido())
				.orElseThrow(() -> new NoSuchElementException("Pedido", itemPedido.getPedido().getIdPedido())));

		emailService.enviarEmail("cesargpmuller@gmail.com", "Assunto entrará aqui.", pedidoDTO.toString());

		return itemSalvo;
	}

	public boolean delete(ItemPedido itemPedido) {
		// verifica se itemPedido é valido
		if (itemPedido == null)
			return false;

		// verifica se existe no banco
		ItemPedido itemPedidoExistente = findById(itemPedido.getIdItemPedido());
		if (itemPedidoExistente == null)
			return false;

		// deleta itemPedido
		itemPedidoRepository.delete(itemPedido);

		// verifica se item foi deletado de fato
		ItemPedido itemPedidoContinuaExistindo = findById(itemPedido.getIdItemPedido());
		if (itemPedidoContinuaExistindo == null) {
			// ATUALIZA O VALOR TOTAL DO PEDIDO REFERENTE AO ITEM
			pedidoService.gerarValorTotal(pedidoRepository.findById(itemPedido.getPedido().getIdPedido())
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
		if (pedido.getItensPedidos() != null && pedido.getItensPedidos().size() > 0) {

			// PREENCHE A RELAÇÃO DE ITENS COM OS ITENS DO PEDIDO
			for (ItemPedido itemPedido : pedido.getItensPedidos()) {

				Produto produto = produtoRepository.findById(itemPedido.getProduto().getIdProduto()).orElseThrow(
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
