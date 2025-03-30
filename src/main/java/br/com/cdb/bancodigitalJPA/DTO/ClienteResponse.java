package br.com.cdb.bancodigitalJPA.DTO;

import java.util.List;

import br.com.cdb.bancodigitalJPA.entity.Conta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;		//eu importei o Lombok pra deixar o código mais limpo, usando Getter pra criar todos/setter tbm
							//o AllArgsConstructor cria o método construtor já.

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {
	private String nome;
	private String cpf;
	private String tipoDeCliente;
	private Long id;
	private Integer idade;
	private EnderecoResponse endereco;
	private List<Conta> contas;
}
