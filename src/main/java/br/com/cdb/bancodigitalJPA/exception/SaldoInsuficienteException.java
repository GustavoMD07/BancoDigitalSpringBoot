package br.com.cdb.bancodigitalJPA.exception;

//criando uma exceção personalizada, por que saldo insuficiente eu vou usar muito
public class SaldoInsuficienteException extends RuntimeException {
	private static final long serialVersionUID = 2884250910798326176L;

	public SaldoInsuficienteException(String mensagem) {
		super(mensagem); //passando a mensagem de erro
	} 
}
