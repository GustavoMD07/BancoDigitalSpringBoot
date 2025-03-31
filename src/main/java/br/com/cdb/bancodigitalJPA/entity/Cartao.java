package br.com.cdb.bancodigitalJPA.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_de_cartao")
@Setter
public abstract class Cartao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "tipo_de_cartao", insertable = false, updatable = false)
	private String tipoDeCartao;
	
	private String senha;
	
	
	private String numCartao;
	
	@ManyToOne
	@JoinColumn(name = "conta_id")
	@JsonBackReference
	private Conta conta;
	
	
	
	
	public Long getId() {
		return id;
	}
	
	public String getSenha() {
		return senha;
	}

	public String getNumCartao() {
		return numCartao;
	}

	public Conta getConta() {
		return conta;
	}
	
	@JsonProperty("tipoDeCartao")
	public String getTipoDeCartao() {
		return tipoDeCartao;
	}
}
