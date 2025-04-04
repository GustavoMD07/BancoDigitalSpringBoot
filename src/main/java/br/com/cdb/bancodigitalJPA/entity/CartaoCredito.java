package br.com.cdb.bancodigitalJPA.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;

@Entity
public class CartaoCredito extends Cartao {
	
	//qualquer coisa, usa o JsonIgnore
	//o double primitivo não tava permitindo que o objeto fosse nulo
	@JsonIgnore
	private Double novoLimiteCredito;
	
	private double limiteCredito;
	
	private double fatura;
	
	public CartaoCredito() {
		
	}
	
	public double getLimiteCredito() {
		if(novoLimiteCredito != null) {
			return novoLimiteCredito;
		}
		return getConta().getCliente().getLimiteCredito();
	}

	public void setLimiteCredito(double novoLimiteCredito) {
		this.novoLimiteCredito = novoLimiteCredito;
	}

	public double getFatura() {
		return fatura;
	}

	public void setFatura(double fatura) {
		this.fatura = fatura;
	}
	
	public Double getNovoLimiteCredito() {
		return novoLimiteCredito;
	}

}
