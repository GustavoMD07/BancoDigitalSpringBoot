package br.com.cdb.bancodigitalJPA.repository;

//import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cdb.bancodigitalJPA.entity.Conta;

//o Repository é literalmente a camada de acesso ao banco de dados

public interface ContaRepository extends JpaRepository<Conta, Long> {

	//aqui eu to buscando todas as contas que tem relacionadas ao cliente
//	List<Conta> contasDeCliente(Long clienteId);
	
}
