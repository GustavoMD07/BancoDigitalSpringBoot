package br.com.cdb.bancodigitalJPA.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cdb.bancodigitalJPA.entity.Cartao;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {

}
