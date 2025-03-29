package br.com.cdb.bancodigitalJPA.DTO;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

//tudo que tá no DTO, é o que eu preciso pra cadastrar no PostMan

public class ClienteDTO {

	@NotBlank(message = "O nome não pode estar vazio.")
	@Size(min = 2, max = 100, message = "O nome deve ter mais de duas letras e menos de 100!")
	@Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "O nome deve conter apenas letras :)")
	private String nome;
	
	@Column(unique = true)
	@NotBlank(message = "O cpf não pode estar vazio.")
	@Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 digitos")
	private String cpf;
	
	@Past
	@NotNull(message = "A data de nascimento não pode estar vazia")
	private LocalDate dataNascimento;
	
	@NotBlank(message = "Selecione um tipo de conta || Comum || Super || Premium")
	private String tipoDeCliente;
	
	
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCPF() {
		return cpf;
	}
	
	public void setCPF(String cpf) {
		this.cpf = cpf.replaceAll("[^\\d]", "");
	}
	
	public LocalDate getDataNascimento() {
		return dataNascimento;
	}
	
	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public String getTipoDeCliente() {
		return tipoDeCliente;
	}
	
	public void setTipoDeCliente(String tipoDeCliente) {
		this.tipoDeCliente = tipoDeCliente;
	}
}
