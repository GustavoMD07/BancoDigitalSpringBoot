package br.com.cdb.bancodigitalJPA.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
public class ContaCorrente extends Conta {

	private double taxaManutencao;

	
	// Se você precisa de um construtor, garanta que o Hibernate consiga usar o no-arg constructor
    public ContaCorrente() {
        // O Hibernate precisa de um construtor sem argumentos
    }

    public double getTaxaManutencao() {
        return taxaManutencao;
    }
    
    @PrePersist
    @PreUpdate
    //uso esse método e notações pra ele calcular o limite antes de colocar a entidade no banco
    //assim o H2 não fica zerado
    public void calcularLimiteAntesDeSalvar() {
        if (this.taxaManutencao == 0.0 && getCliente() != null) {
            this.taxaManutencao = getCliente().getTaxaManutencao();
        }
    }

}
