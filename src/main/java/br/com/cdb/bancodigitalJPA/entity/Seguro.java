package br.com.cdb.bancodigitalJPA.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Seguro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String numeroApolice;
	private LocalDate dataContratacao;
	private String tipoDeSeguro;
	private String descricao;
	private double valorApolice;
	private boolean ativo;
	
	@ManyToOne
	@JoinColumn(name = "cartao_id") //mapeia a FK com o nome de cartao_id
	private CartaoCredito cartao;
	
}
