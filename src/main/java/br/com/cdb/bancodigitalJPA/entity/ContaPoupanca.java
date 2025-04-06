package br.com.cdb.bancodigitalJPA.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "idConta")
public class ContaPoupanca extends Conta {

	private double taxaRendimento;
	
	// Se você precisa de um construtor, garanta que o Hibernate consiga usar o no-arg constructor
    public ContaPoupanca() {
        // O Hibernate precisa de um construtor sem argumentos
    }

    public double getTaxaRendimento() {
        return taxaRendimento;
    }
    
    @PrePersist
    @PreUpdate
    //uso esse método e notações pra ele calcular o limite antes de colocar a entidade no banco
    //assim o H2 não fica zerado
    public void calcularLimiteAntesDeSalvar() {
        if (this.taxaRendimento == 0.0 && getCliente() != null) {
            this.taxaRendimento = getCliente().getTaxaRendimento();
        }
    }

}
