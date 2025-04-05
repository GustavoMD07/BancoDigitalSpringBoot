package br.com.cdb.bancodigitalJPA.DTO;

import java.util.List;

import br.com.cdb.bancodigitalJPA.entity.Cliente;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; //eu importei o Lombok pra deixar o código mais limpo, usando Getter pra criar todos/setter tbm
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
	

	public static ClienteResponse fromEntity(Cliente cliente) {
		ClienteResponse clienteResponse = new ClienteResponse();
		clienteResponse.setNome(cliente.getNome());
		clienteResponse.setCpf(cliente.getCpf());
		clienteResponse.setTipoDeCliente(cliente.getTipoDeCliente());
		clienteResponse.setId(cliente.getId());
		clienteResponse.setIdade(cliente.getIdade());
		
		// monta o endereço com os dados do cliente
		EnderecoResponse endereco = new EnderecoResponse();
		endereco.setCep(cliente.getCep());
		endereco.setRua(cliente.getRua());
		endereco.setEstado(cliente.getEstado());
		endereco.setCidade(cliente.getCidade());
		endereco.setBairro(cliente.getBairro());
		clienteResponse.setEndereco(endereco);

		clienteResponse.setContas(cliente.getContas());
		return clienteResponse;
	}
}
