package br.org.serratec.ecommerce.dtos;

public class EnderecoDTO {

	private Long idEndereco;
	private String rua;
	private String bairro;
	private String cidade;
	private Integer numero;
	private String complemento;
	private String uf;

	public EnderecoDTO() {
	}

	public EnderecoDTO(Long idEndereco, String rua, String bairro, String cidade, Integer numero, String complemento,
			String uf) {
		this.idEndereco = idEndereco;
		this.rua = rua;
		this.bairro = bairro;
		this.cidade = cidade;
		this.numero = numero;
		this.complemento = complemento;
		this.uf = uf;
	}

	@Override
	public String toString() {
		return "EnderecoDTO [idEndereco=" + idEndereco + ", rua=" + rua + ", bairro=" + bairro + ", cidade=" + cidade
				+ ", numero=" + numero + ", complemento=" + complemento + ", uf=" + uf + "]";
	}

	public Long getIdEndereco() {
		return idEndereco;
	}

	public void setIdEndereco(Long idEndereco) {
		this.idEndereco = idEndereco;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

}
