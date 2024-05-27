package com.residencia.ecommerce.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.residencia.ecommerce.dto.ItemPedidoDTO;
import com.residencia.ecommerce.dto.RelatorioPedidoDTO;
import com.residencia.ecommerce.entities.ItemPedido;
import com.residencia.ecommerce.entities.Pedido;
import com.residencia.ecommerce.entities.Produto;
import com.residencia.ecommerce.exceptions.NoSuchElementException;
import com.residencia.ecommerce.repositories.PedidoRepository;
import com.residencia.ecommerce.repositories.ProdutoRepository;

@Service
public class PedidoService {

	@Autowired
	PedidoRepository pedidoRepo;

	@Autowired
	ProdutoRepository produtoRepo;

	@Autowired
	EmailService emailService;

	public List<RelatorioPedidoDTO> listarPedidos() {
		List<Pedido> pedidos = pedidoRepo.findAll();
		List<RelatorioPedidoDTO> pedidosDTO = new ArrayList<>();
		
		// CONVERTE ITENS DA LISTA EM DTO
		for(Pedido pedido : pedidos) {
			pedidosDTO.add(convertPedidoToDTO(pedido));
		}
		
		// RETORNA LISTA DE DTO
		return pedidosDTO;
	}

	public Pedido getPedidoPorId(Long id) {
		return pedidoRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Pedido", id));
	}

	public RelatorioPedidoDTO salvarPedido(Pedido pedido) {
		// SALVA O PRODUTO
		Pedido pedidoSalvo = pedidoRepo.save(pedido);
		
		// GERA RELATORIO
		RelatorioPedidoDTO pedidoDTO = gerarRelatorioDTO(pedidoSalvo);

		// ENVIA EMAIL
		emailService.enviarEmail("ikaro.gaspar1@gmail.com", "Assunto entrará aqui.", pedidoDTO.toString());
		
		// RETORNA DTO
		return pedidoDTO;
	}

	public Pedido atualizarPedido(Pedido pedido) {

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
			pedidoRepo.save(pedidoSalvo);

			break;

		case "ENTREGUE":

			// ATUALIZA DATA DE ENTREGA
			pedidoSalvo.setDataEntrega(new Date());

			// ATUALIZA PEDIDO
			pedidoRepo.save(pedidoSalvo);

			// GERA RELATORIO
			pedidoDTO = gerarRelatorioDTO(pedidoSalvo);

			// ENVIA EMAIL
			emailService.enviarEmail("ikaro.gaspar1@gmail.com", "Assunto entrará aqui.", pedidoDTO.toString());
			break;
		default:
			// ATUALIZA PEDIDO
			pedidoRepo.save(pedidoSalvo);
			break;

		}

		// RETORNA PEDIDO SALVO COMO RESPOSTA
		return pedidoSalvo;
	}

	public boolean deletarPedido(Pedido pedido) {
		// verifica se pedido é valido
		if (pedido == null)
			return false;

		// verifica se existe no banco
		Pedido pedidoExistente = getPedidoPorId(pedido.getIdPedido());
		if (pedidoExistente == null)
			return false;

		// deleta pedido
		pedidoRepo.delete(pedido);

		// verifica se foi deletado de fato
		Pedido pedidoContinuaExistindo = getPedidoPorId(pedido.getIdPedido());
		if (pedidoContinuaExistindo == null)
			return true;
		return false;
	}

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
		return new RelatorioPedidoDTO(pedido.getIdPedido(), pedido.getDataPedido(), pedido.getValorTotal(),
				itensPedidosDTO);
	}

	public RelatorioPedidoDTO gerarRelatorioDTO(Pedido pedido) {
		return convertPedidoToDTO(pedido);
	}

	public void gerarValorTotal(Pedido pedido) {
		BigDecimal valorTotal = new BigDecimal(0);
		if (pedido.getItemPedidos() != null) {
			for (ItemPedido itemPedido : pedido.getItemPedidos()) {
				valorTotal = valorTotal.add(itemPedido.getValorLiquido());
			}
			System.out.println("\n\n\n\n" + valorTotal + "\n\n\n\n\n\n");
		} else {
			System.out.println("\n\n\n\nitem pedido e nulo\n\n\n\n\n");
		}
		pedido.setValorTotal(valorTotal);
		pedidoRepo.save(pedido);
	}
}