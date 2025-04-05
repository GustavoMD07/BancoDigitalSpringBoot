package br.com.cdb.bancodigitalJPA.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_de_conta")
public abstract class Conta {							
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //ID vai ser incremento (1 a 1)
	private Long id;
	private Double saldo;
	
	//ManyToOne - posso ter mais de uma conta, mas todas elas vão ter que ser relacionadas a um só objeto
	//o cliente :)
	//O JoinColumn é responsável por criar a Foreing Key
	@ManyToOne
	@JoinColumn(name = "cliente_id")		//BackReference pra ele não ficar infinito no JSON
	@JsonBackReference
	private Cliente cliente;
	//esse atributo eu mapeio ele no cliente, depois passo o nome dele no Cliente
	

	@OneToMany(mappedBy = "conta", fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<Cartao> cartao;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public List<Cartao> getCartao() {
		return cartao;
	}

	public void setCartao(List<Cartao> cartao) {
		this.cartao = cartao;
	}

	
	@JsonProperty		//JsonProperty garante que ele vá aparecer no PostMan
	public String getTipoDeConta() {
		return this.getClass().getSimpleName().replace("Conta", "");
	}
}
