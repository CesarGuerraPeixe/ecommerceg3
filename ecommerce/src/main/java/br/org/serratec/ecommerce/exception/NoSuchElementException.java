package br.org.serratec.ecommerce.exception;

public class NoSuchElementException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NoSuchElementException(String message) {
		super(message);
	}

	public NoSuchElementException(String entidade, Long id) {
		super("Não foi encontrado(a) " + entidade + " com o id = " + id);
	}
}
