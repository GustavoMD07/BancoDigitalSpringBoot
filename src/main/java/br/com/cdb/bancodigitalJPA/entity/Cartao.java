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

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_de_cartao")
public abstract class Cartao {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "tipo_de_cartao", insertable = false, updatable = false)
	private String tipoDeCartao;
	
	private String senha;
	private boolean status;
	
	@Column(unique = true)
	private String numCartao;
	
	@ManyToOne
	@JoinColumn(name = "conta_id")
	@JsonBackReference
	protected Conta conta;

	
	public Long getId() {
		return id;
	}
	
	public String getSenha() {
		return senha;
	}

	public String getNumCartao() {
		return numCartao;
	}
	
	@JsonProperty("tipoDeCartao")
	public String getTipoDeCartao() {
		return tipoDeCartao;
	}

	public Conta getConta() {
		return conta;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setTipoDeCartao(String tipoDeCartao) {
		this.tipoDeCartao = tipoDeCartao;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setNumCartao(String numCartao) {
		this.numCartao = numCartao;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
