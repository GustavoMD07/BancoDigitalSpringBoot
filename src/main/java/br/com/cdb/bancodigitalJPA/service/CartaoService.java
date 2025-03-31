package br.com.cdb.bancodigitalJPA.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitalJPA.DTO.CartaoDTO;
import br.com.cdb.bancodigitalJPA.entity.Cartao;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import br.com.cdb.bancodigitalJPA.exception.ObjetoNuloException;
import br.com.cdb.bancodigitalJPA.repository.CartaoRepository;
import br.com.cdb.bancodigitalJPA.repository.ContaRepository;

@Service
public class CartaoService {

	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Autowired
	private ContaRepository contaRepository;
	
	public Cartao addCartao(CartaoDTO cartaoDto) {
		Optional<Conta> conta = contaRepository.findById(cartaoDto.getContaId());
		
		if(conta.isEmpty()) {
			throw new ObjetoNuloException("Conta não encontrada");
		}
		
	}
}
