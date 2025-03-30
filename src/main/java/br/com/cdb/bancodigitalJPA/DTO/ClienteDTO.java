package br.com.cdb.bancodigitalJPA.DTO;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

//tudo que tá no DTO, é o que eu preciso pra cadastrar no PostMan

@Getter
public class ClienteDTO {

	@NotBlank(message = "O nome não pode estar vazio.")
	@Size(min = 2, max = 100, message = "O nome deve ter mais de duas letras e menos de 100!")
	@Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "O nome deve conter apenas letras :)")
	private String nome;
	
	@Column(unique = true)
	@NotBlank(message = "O cpf não pode estar vazio.")
	@Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 digitos")
	private String CPF;
	
	@Past
	@NotNull(message = "A data de nascimento não pode estar vazia")
	private LocalDate dataNascimento;
	
	@NotBlank(message = "Selecione um tipo de conta || Comum || Super || Premium")
	private String tipoDeCliente;
	
	@NotBlank(message = "o CEP não pode estar vazio")
	@Pattern(regexp = "\\d{8}", message = "O CEP deve conter 8 digítos")
	private String cep;

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	public void setCPF(String cpf) {
		this.CPF = cpf.replaceAll("[^\\d]", ""); //tiro qualquer traço/ponto/espaço do cpf
	}
	
	
	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public void setTipoDeCliente(String tipoDeCliente) {
		this.tipoDeCliente = tipoDeCliente;
	}
	

	public void setCep(String cep) {
		this.cep = cep.replaceAll("[^\\d]", "");
	}
}
