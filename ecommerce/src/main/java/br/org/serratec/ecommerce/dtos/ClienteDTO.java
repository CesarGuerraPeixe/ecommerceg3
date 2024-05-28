package br.org.serratec.ecommerce.dtos;

public class ClienteDTO {

	private Long idCliente;
	private String email;
	private String nomeCompleto;
	private String telefone;

	public ClienteDTO() {
	}

	public ClienteDTO(Long idCliente, String email, String nomeCompleto, String telefone) {
		super();
		this.idCliente = idCliente;
		this.email = email;
		this.nomeCompleto = nomeCompleto;
		this.telefone = telefone;
	}

	@Override
	public String toString() {
		return "ClienteDTO [idCliente=" + idCliente + ", email=" + email + ", nomeCompleto=" + nomeCompleto
				+ ", telefone=" + telefone + "]";
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
}