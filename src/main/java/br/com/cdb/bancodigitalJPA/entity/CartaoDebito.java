package br.com.cdb.bancodigitalJPA.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "idCartao")
public class CartaoDebito extends Cartao {

	private double limiteDiario = 500;
	

	public CartaoDebito() {
		
	}
	
	public double getLimiteDiario() {
		return limiteDiario;
	}


	public void setLimiteDiario(double limiteDiario) {
		this.limiteDiario = limiteDiario;
	}

}
