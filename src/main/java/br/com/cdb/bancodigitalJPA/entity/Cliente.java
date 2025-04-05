package br.com.cdb.bancodigitalJPA.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;


//no entity mesmo, ele já cria a tabela no banco de dados (h2) ele já "Mapeia"
//a gente pensa no Entity como se fosse a "interface" da tabela do nosso banco, é o que você guarda

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_de_cliente")
public abstract class Cliente {

	// gerando ID de forma icrementada
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // ID vai ser incremento (1 a 1)
	private Long id;
	//usando o Bean Validation, ele é um padrão de mercado, então é bom usar
	
	//cpf tem que ser único
	@Column(unique = true)
	private String cpf;
	private Integer idade;
	private String nome;
	
									//logo ao criar um atributo no Entity, ele já cria essa coluna no H2
	private String cep;				//isso tudo vai ser mostrado no H2, que é o nosso banco de dados

	private String estado;			//o que for mostrado no Postman, vai ser o Response de cada entidade
	private String cidade;
	private String bairro;
	private String rua;
	
	public abstract double getTaxaManutencao();
	public abstract double getTaxaRendimento();
	public abstract double getLimiteCredito();
	
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
	
	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}
	
	@JsonProperty("tipoDeCliente")
	public String getTipoDeCliente() {
		return this.getClass().getSimpleName().replace("Cliente", "");
	}

	
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}
}
