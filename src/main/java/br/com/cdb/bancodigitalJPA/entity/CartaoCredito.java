package br.com.cdb.bancodigitalJPA.entity;

import br.com.cdb.bancodigitalJPA.service.ClienteService;
import jakarta.persistence.Entity;

@Entity
public class CartaoCredito extends Cartao {
	
	
	private double limiteCredito = 5000;
	
	private double fatura;
	
	public CartaoCredito() {
		
	}
	
	public double getLimiteCredito() {
		return limiteCredito;
	}

	public void setLimiteCredito(double limiteCredito) {
		this.limiteCredito = limiteCredito;
	}

	public double getFatura() {
		return fatura;
	}

	public void setFatura(double fatura) {
		this.fatura = fatura;
	}
}
