package br.com.cdb.bancodigitalJPA.exception;

//criando uma exceção personalizada, por que saldo insuficiente eu vou usar muito
public class SaldoInsuficienteException extends RuntimeException {

	
	public SaldoInsuficienteException(String mensagem) {
		super(mensagem); //passando a mensagem de erro
	} 
}
