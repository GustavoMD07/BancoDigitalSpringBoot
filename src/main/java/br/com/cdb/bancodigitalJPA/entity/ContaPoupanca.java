package br.com.cdb.bancodigitalJPA.entity;

import jakarta.persistence.Entity;

@Entity
public class ContaPoupanca extends Conta {

	private double taxaRendimento = 0.005;
	
	// Se você precisa de um construtor, garanta que o Hibernate consiga usar o no-arg constructor
    public ContaPoupanca() {
        // O Hibernate precisa de um construtor sem argumentos
    }

    public Double getTaxaManutencao() {
        return taxaRendimento;
    }

}
