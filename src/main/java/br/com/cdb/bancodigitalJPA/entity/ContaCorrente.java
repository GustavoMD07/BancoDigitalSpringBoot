package br.com.cdb.bancodigitalJPA.entity;

import jakarta.persistence.Entity;

@Entity
public class ContaCorrente extends Conta {


	
	// Se você precisa de um construtor, garanta que o Hibernate consiga usar o no-arg constructor
    public ContaCorrente() {
        // O Hibernate precisa de um construtor sem argumentos
    }

    public double getTaxaManutencao() {
        return getCliente().getTaxaManutencao();
    }

}
