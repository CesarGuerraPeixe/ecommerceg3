package br.org.serratec.ecommerce.dtos;

import java.math.BigDecimal;

public class ProdutoDTO {

	private Long idProduto;
	private String nome;
	private String descricao;
	private Integer qtdEstoque;
	private BigDecimal valorUnitario;

	public ProdutoDTO() {
	}

	public ProdutoDTO(Long idProduto, String nome, String descricao, Integer qtdEstoque, BigDecimal valorUnitario) {
		this.idProduto = idProduto;
		this.nome = nome;
		this.descricao = descricao;
		this.qtdEstoque = qtdEstoque;
		this.valorUnitario = valorUnitario;
	}

	@Override
	public String toString() {
		return "ProdutoDTO [idProduto=" + idProduto + ", nome=" + nome + ", descricao=" + descricao + ", qtdEstoque="
				+ qtdEstoque + ", valorUnitario=" + valorUnitario + "]";
	}

	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getQtdEstoque() {
		return qtdEstoque;
	}

	public void setQtdEstoque(Integer qtdEstoque) {
		this.qtdEstoque = qtdEstoque;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

}