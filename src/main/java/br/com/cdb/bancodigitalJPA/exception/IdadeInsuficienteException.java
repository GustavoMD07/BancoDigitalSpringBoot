package br.com.cdb.bancodigitalJPA.exception;

public class IdadeInsuficienteException extends RuntimeException {

	private static final long serialVersionUID = -6233317663041840422L;
	
	public IdadeInsuficienteException(String mensagem) {
		super(mensagem);
	}

}
