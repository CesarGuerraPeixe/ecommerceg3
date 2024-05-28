package br.org.serratec.ecommerce.exception;

public class PSQLException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PSQLException(String entidade, String campo) {
		super("A entidade " + entidade + " possui o campo " + campo
				+ " já cadastrado no Banco de Dados. Cheque novamente os campos.");
	}
}
