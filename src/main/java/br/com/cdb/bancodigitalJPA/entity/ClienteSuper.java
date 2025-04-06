package br.com.cdb.bancodigitalJPA.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "idCliente")
public class ClienteSuper extends Cliente {

	private double limiteCredito = 5000;
	private double taxaRendimento = 0.007;
	private double taxaManutencao = 8;
	private double valorSeguroViagem = 50;
	
	
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
	
	public double getValorSeguroViagem() {
		return valorSeguroViagem;
	}

	public void setValorSeguroViagem(double valorSeguroViagem) {
		this.valorSeguroViagem = valorSeguroViagem;
	}

}
