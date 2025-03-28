package br.com.cdb.bancodigitalJPA.repository;
import java.util.Optional;

//esse aqui é tipo o DAO, onde vc vai poder guardar, remover, listar todos, listar
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitalJPA.entity.Cliente;
//to fazendo uma interface do JPA  //o que o repository tá guardando? Cliente

@Repository 
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	//qual o tipo da primarKey? Long, que é um ID. :) //a gente usa o JDA
	//save, delete, list, listAll, update, tá tudo dentro do JPA
	
	Optional<Cliente> findByCpf(String cpf);
}
