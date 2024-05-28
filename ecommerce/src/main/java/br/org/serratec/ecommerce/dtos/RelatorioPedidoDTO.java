package br.org.serratec.ecommerce.dtos;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class RelatorioPedidoDTO {

	private Long idPedido;

	private Date dataPedido;

	private BigDecimal valorTotal;

	private List<ItemPedidoDTO> itemPedidos;

	public RelatorioPedidoDTO() {

	}

	public RelatorioPedidoDTO(Long idPedido, Date dataPedido, BigDecimal valorTotal, List<ItemPedidoDTO> itemPedidos) {
		this.idPedido = idPedido;
		this.dataPedido = dataPedido;
		this.valorTotal = valorTotal;
		this.itemPedidos = itemPedidos;
	}

	public Long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	public Date getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(Date dataPedido) {
		this.dataPedido = dataPedido;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public List<ItemPedidoDTO> getItemPedidos() {
		return itemPedidos;
	}

	public void setItemPedidos(List<ItemPedidoDTO> itemPedidos) {
		this.itemPedidos = itemPedidos;
	}

	@Override
	public String toString() {
		return "************** RELATÓRIO PEDIDO **************\n\n" + "Id do Pedido: " + idPedido + "\nData do Pedido: "
				+ dataPedido + "\nValor Total: " + valorTotal + "\n\n********* ITENS PEDIDOS *********\n\n"
				+ itemPedidos;
	}

}
