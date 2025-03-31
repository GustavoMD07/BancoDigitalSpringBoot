package br.com.cdb.bancodigitalJPA.exception;

public class ApiBloqueadaException extends RuntimeException {
	private static final long serialVersionUID = 4616126302068753005L;
	
	public ApiBloqueadaException(String mensagem) {
		super(mensagem);
	}

}
