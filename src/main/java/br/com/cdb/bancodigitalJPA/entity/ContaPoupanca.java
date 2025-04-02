package br.com.cdb.bancodigitalJPA.entity;

import jakarta.persistence.Entity;

@Entity
public class ContaPoupanca extends Conta {

	
	// Se você precisa de um construtor, garanta que o Hibernate consiga usar o no-arg constructor
    public ContaPoupanca() {
        // O Hibernate precisa de um construtor sem argumentos
    }

    public double getTaxaRendimento() {
        return getCliente().getTaxaRendimento();
    }

}
