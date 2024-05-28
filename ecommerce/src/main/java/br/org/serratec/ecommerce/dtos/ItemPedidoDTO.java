package br.org.serratec.ecommerce.dtos;

import java.math.BigDecimal;

public class ItemPedidoDTO {

	private Long idItemPedido;
	private String nomeProduto;
	private BigDecimal precoVenda;
	private Integer quantidade;
	private BigDecimal valorBruto;
	private BigDecimal percentualDesconto;
	private BigDecimal valorLiquido;

	public ItemPedidoDTO() {

	}

	public ItemPedidoDTO(Long idItemPedido, String nomeProduto, BigDecimal precoVenda, Integer quantidade,
			BigDecimal valorBruto, BigDecimal percentualDesconto, BigDecimal valorLiquido) {

		this.idItemPedido = idItemPedido;
		this.nomeProduto = nomeProduto;
		this.precoVenda = precoVenda;
		this.quantidade = quantidade;
		this.valorBruto = valorBruto;
		this.percentualDesconto = percentualDesconto;
		this.valorLiquido = valorLiquido;
	}

	public Long getIdItemPedido() {
		return idItemPedido;
	}

	public void setIdItemPedido(Long idItemPedido) {
		this.idItemPedido = idItemPedido;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(BigDecimal valorBruto) {
		this.valorBruto = valorBruto;
	}

	public BigDecimal getPercentualDesconto() {
		return percentualDesconto;
	}

	public void setPercentualDesconto(BigDecimal percentualDesconto) {
		this.percentualDesconto = percentualDesconto;
	}

	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	@Override
	public String toString() {
		return "Id do ItemPedido: " + idItemPedido + "\nNome do Produto: " + nomeProduto + "\nPreco da Venda: "
				+ precoVenda + "\nQuantidade: " + quantidade + "\nValor Bruto: " + valorBruto
				+ "\nPorcentagem de Desconto: " + percentualDesconto + "\nValor Economizado: "
				+ (valorBruto.multiply(percentualDesconto)) + "\nValor Líquido: " + valorLiquido + "\n";
	}

}
