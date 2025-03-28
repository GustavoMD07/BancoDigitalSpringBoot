package br.com.cdb.bancodigitalJPA.entity;

import jakarta.persistence.Entity;

@Entity
public class ContaCorrente extends Conta {

	private double taxaManutencao = 10;
	
	// Se você precisa de um construtor, garanta que o Hibernate consiga usar o no-arg constructor
    public ContaCorrente() {
        // O Hibernate precisa de um construtor sem argumentos
    }

    public Double getTaxaManutencao() {
        return taxaManutencao;
    }

}
