package br.com.cdb.bancodigitalJPA.exception;

public class QuantidadeExcedidaException extends RuntimeException {

	private static final long serialVersionUID = -7985658313510537060L;

	public QuantidadeExcedidaException(String mensagem) {
		super(mensagem);
	}
}
