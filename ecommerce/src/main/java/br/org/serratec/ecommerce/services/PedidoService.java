package br.org.serratec.ecommerce.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.serratec.ecommerce.dtos.ItemPedidoDTO;
import br.org.serratec.ecommerce.dtos.RelatorioPedidoDTO;
import br.org.serratec.ecommerce.entities.ItemPedido;
import br.org.serratec.ecommerce.entities.Pedido;
import br.org.serratec.ecommerce.entities.Produto;
import br.org.serratec.ecommerce.exception.NoSuchElementException;
import br.org.serratec.ecommerce.repositories.PedidoRepository;
import br.org.serratec.ecommerce.repositories.ProdutoRepository;

@Service
public class PedidoService {

	@Autowired
	PedidoRepository pedidoRepository;

	@Autowired
	ProdutoRepository produtoRepository;

	@Autowired
	EmailService emailService;

	public List<RelatorioPedidoDTO> findAll() {
		List<Pedido> pedidos = pedidoRepository.findAll();
		List<RelatorioPedidoDTO> pedidosDTO = new ArrayList<>();

		// CONVERTE ITENS DA LISTA EM DTO
		for (Pedido pedido : pedidos) {
			pedidosDTO.add(convertPedidoToDTO(pedido));
		}

		// RETORNA LISTA DE DTO
		return pedidosDTO;
	}

	public Pedido findById(Long id) {
		return pedidoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Pedido", id));
	}

	public RelatorioPedidoDTO save(Pedido pedido) {
		// SALVA O PRODUTO
		Pedido pedidoSalvo = pedidoRepository.save(pedido);

		// GERA RELATORIO
		RelatorioPedidoDTO pedidoDTO = gerarRelatorioDTO(pedidoSalvo);

		// ENVIA EMAIL
		emailService.enviarEmail("APIserratecGrupo3@gmail.com", "Seu pedido foi realizado com sucesso!", pedidoDTO.toString());

		// RETORNA DTO
		return pedidoDTO;
	}

	public Pedido update(Pedido pedido) {

		// CRIA PEDIDO QUE SERA SALVO
		Pedido pedidoSalvo = pedido;

		// GERA RELATORIO
		RelatorioPedidoDTO pedidoDTO;

		// VERIFICA O STATUS DO PEDIDO
		switch (pedido.getStatus().toUpperCase()) {
		case "ENVIADO":

			// ATUALIZA DATA DE ENVIO
			pedidoSalvo.setDataEnvio(new Date());

			// ATUALIZA PEDIDO
			pedidoRepository.save(pedidoSalvo);

			break;

		case "ENTREGUE":

			// ATUALIZA DATA DE ENTREGA
			pedidoSalvo.setDataEntrega(new Date());

			// ATUALIZA PEDIDO
			pedidoRepository.save(pedidoSalvo);

			// GERA RELATORIO
			pedidoDTO = gerarRelatorioDTO(pedidoSalvo);

			// ENVIA EMAIL
			emailService.enviarEmail("APIserratecGrupo3@gmail.com", "Seu pedido foi entregue com sucesso!", pedidoDTO.toString());
			break;
		default:
			// ATUALIZA PEDIDO
			pedidoRepository.save(pedidoSalvo);
			break;

		}

		// RETORNA PEDIDO SALVO COMO RESPOSTA
		return pedidoSalvo;
	}

	public boolean delete(Pedido pedido) {
		// verifica se pedido é valido
		if (pedido == null)
			return false;

		// verifica se existe no banco
		Pedido pedidoExistente = findById(pedido.getIdPedido());
		if (pedidoExistente == null)
			return false;

		// deleta pedido
		pedidoRepository.delete(pedido);

		// verifica se foi deletado de fato
		Pedido pedidoContinuaExistindo = findById(pedido.getIdPedido());
		if (pedidoContinuaExistindo == null)
			return true;
		return false;
	}

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
		return new RelatorioPedidoDTO(pedido.getIdPedido(), pedido.getDataPedido(), pedido.getValorTotal(),
				itensPedidosDTO);
	}

	public RelatorioPedidoDTO gerarRelatorioDTO(Pedido pedido) {
		return convertPedidoToDTO(pedido);
	}

	public void gerarValorTotal(Pedido pedido) {
		BigDecimal valorTotal = new BigDecimal(0);
		if (pedido.getItensPedidos() != null) {
			for (ItemPedido itemPedido : pedido.getItensPedidos()) {
				valorTotal = valorTotal.add(itemPedido.getValorLiquido());
			}
			System.out.println("\n\n\n\n" + valorTotal + "\n\n\n\n\n\n");
		} else {
			System.out.println("\n\n\n\nitem pedido e nulo\n\n\n\n\n");
		}
		pedido.setValorTotal(valorTotal);
		pedidoRepository.save(pedido);
	}
}
