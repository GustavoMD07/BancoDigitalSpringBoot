package br.com.cdb.bancodigitalJPA.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity // no entity mesmo, ele já cria a tabela no banco de dados (h2) ele já "Mapeia"
//a gente pensa no Entity como se fosse a "interface" da tabela do nosso banco, é o que você guarda
public class Cliente {

	// gerando ID de forma icrementada
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // ID vai ser incremento (1 a 1)
	private Long id;
	//usando o Bean Validation, ele é um padrão de mercado, então é bom usar
	
	//cpf tem que ser único
	@Column(unique = true)
	@NotBlank(message = "O cpf não pode estar vazio.")
	@Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 digitos")
	private String cpf;
	
	
	
	@NotBlank(message = "O nome não pode estar vazio.")
	@Size(min = 2, max = 100, message = "O nome deve ter mais de duas letras e menos de 100!")
	@Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "O nome deve conter apenas letras :)")
	private String nome;
	
	@OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<Conta> contas;
	//por que usar? 1- Ele impede a criação de um cliente_id no cliente
	//2- vai ser a Entidade Conta que vai ter o cliente_id
	//estamos apontando que o cliente é dono da conta, que ele é o dono da Foreing Key
	//se eu não colocar o mappedBy, o JPA ia criar outra Foreing Key
	//no mappedBy, eu preciso passar o nome do atributo na Conta que representa o cliente
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf.replaceAll("[^\\d]", "");
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Conta> getContas() {
		return contas;
	}

	public void setContas(List<Conta> contas) {
		this.contas = contas;
	}
}
