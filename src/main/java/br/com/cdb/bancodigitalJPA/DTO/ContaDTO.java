package br.com.cdb.bancodigitalJPA.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// o banco de dados (H2) segue a estrutura do entity
// o DTO vai definir o que vai para o Postman e o que vem do Postman
// o que eu mando no Postman TEM QUE SER igual ao DTO que o Controller espera

@Getter
@Setter
public class ContaDTO {

	@NotNull(message = "O saldo não pode ser nulo")
	private Double saldo;
	
	@NotNull(message = "É necessário passar seu ID de cliente para podermos associar a sua conta!")
	private Long clienteId;
	
	@NotBlank(message = "Informe o tipo de conta: Corrente || Poupança")
	private String tipoDeConta;
	
	
}
