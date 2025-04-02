package br.com.cdb.bancodigitalJPA.entity;

import jakarta.persistence.Entity;

@Entity
public class ClienteSuper extends Cliente {

	@Override
	public double getTaxaManutencao() {
		return 8.00;
	}

	@Override
	public double getTaxaRendimento() {
		return 0.009;
	}

	@Override
	public double getLimiteCredito() {
		return 5000;
	}

}
