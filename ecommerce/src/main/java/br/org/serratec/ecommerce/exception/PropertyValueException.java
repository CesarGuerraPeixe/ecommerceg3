package br.org.serratec.ecommerce.exception;

public class PropertyValueException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PropertyValueException(String entidade) {
		super("A entidade " + entidade + " possui campos que não podem ser nulos. Cheque os campos.");
	}
}
