package br.com.cdb.bancodigitalJPA.entity;

import jakarta.persistence.Entity;

@Entity
public class CartaoCredito extends Cartao {

	private double limiteCredito = 5000;
	private double fatura;
	
	public CartaoCredito() {
		
	}
}
