package br.com.cdb.bancodigitalJPA.exception;

public class ObjetoNuloException extends RuntimeException {
	private static final long serialVersionUID = 6031873861852735710L;

	public ObjetoNuloException(String mensagem) {
		super(mensagem);
	}
}
