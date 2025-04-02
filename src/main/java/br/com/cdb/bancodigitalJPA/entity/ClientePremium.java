package br.com.cdb.bancodigitalJPA.entity;

import jakarta.persistence.Entity;

@Entity
public class ClientePremium extends Cliente {

	@Override
	public double getTaxaManutencao() {
		return 0;
	}

	@Override
	public double getTaxaRendimento() {
		return 0.007;
	}

	@Override
	public double getLimiteCredito() {
		return 10000;
	}

}
