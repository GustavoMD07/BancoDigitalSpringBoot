package br.com.cdb.bancodigitalJPA.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //juntar as classes filhas na mesma tabela
@DiscriminatorColumn(name = "tipo_de_conta")    //a discriminatória vai ser o tipo de conta
public abstract class Conta {							//esse é o parâmetro pra diferenciar
	


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
}
