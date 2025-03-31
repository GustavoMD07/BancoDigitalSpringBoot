package br.com.cdb.bancodigitalJPA.entity;

import jakarta.persistence.Entity;

@Entity
public class CartaoDebito extends Cartao {

	private double limiteDiario = 400;
	
	
	public CartaoDebito() {
		
	}
}
