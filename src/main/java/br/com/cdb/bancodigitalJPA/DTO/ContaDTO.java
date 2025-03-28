package br.com.cdb.bancodigitalJPA.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// o banco de dados (H2) segue a estrutura do entity
// o DTO vai definir o que vai para o Postman e o que vem do Postman
// o que eu mando no Postman TEM QUE SER igual ao DTO que o Controller espera

public class ContaDTO {

	@NotNull(message = "O saldo não pode ser nulo")
	private Double saldo;
	
	@NotNull(message = "É necessário passar seu ID de cliente para podermos associar a sua conta!")
	private Long clienteId;
	
	@NotBlank(message = "Informe o tipo de conta: Corrente || Poupança")
	private String tipoDeConta;
	
	
	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public String getTipoDeConta() {
		return tipoDeConta;
	}

	public void setTipoDeConta(String tipoDeConta) {
		this.tipoDeConta = tipoDeConta;
	}
}
