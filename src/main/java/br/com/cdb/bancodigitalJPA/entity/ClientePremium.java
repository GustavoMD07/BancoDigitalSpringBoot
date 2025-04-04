package br.com.cdb.bancodigitalJPA.entity;

import jakarta.persistence.Entity;

@Entity
public class ClientePremium extends Cliente {

	private double limiteCredito = 10000;
	private double taxaRendimento = 0.009;
	private double taxaManutencao = 0;
	
	@Override
	public double getTaxaManutencao() {
		return taxaManutencao;
	}

	@Override
	public double getTaxaRendimento() {
		return taxaRendimento;
	}

	@Override
	public double getLimiteCredito() {
		return limiteCredito;
	}
	
	public void setLimiteCredito(double limiteCredito) {
		this.limiteCredito = limiteCredito;
	}

	public void setTaxaRendimento(double taxaRendimento) {
		this.taxaRendimento = taxaRendimento;
	}

	public void setTaxaManutencao(double taxaManutencao) {
		this.taxaManutencao = taxaManutencao;
	}

}
