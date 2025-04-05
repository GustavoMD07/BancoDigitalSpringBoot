package br.com.cdb.bancodigitalJPA.entity;

import jakarta.persistence.Entity;

@Entity
public class ClienteComum extends Cliente {

	
	
	@Override
	public double getTaxaManutencao() {
		return 12.00;
	}

	@Override
	public double getTaxaRendimento() {
		return 0.005;
	}

	@Override
	public double getLimiteCredito() {
		return 1000;
	}

}
