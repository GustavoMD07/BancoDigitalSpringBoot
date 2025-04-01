package br.com.cdb.bancodigitalJPA.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigitalJPA.DTO.CartaoDTO;
import br.com.cdb.bancodigitalJPA.entity.Cartao;
import br.com.cdb.bancodigitalJPA.entity.CartaoCredito;
import br.com.cdb.bancodigitalJPA.entity.CartaoDebito;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import br.com.cdb.bancodigitalJPA.exception.ListaVaziaException;
import br.com.cdb.bancodigitalJPA.repository.ContaRepository;
import br.com.cdb.bancodigitalJPA.service.CartaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

	@Autowired
	private CartaoService cartaoService;
	
	@Autowired
	private ContaRepository contaRepository;
	
	@PostMapping("/add")
	public ResponseEntity<String> addCartao(@RequestBody @Valid CartaoDTO cartaoDto) {
		Optional<Conta> contaProcurada = contaRepository.findById(cartaoDto.getContaId());
		
		if(contaProcurada.isEmpty()) {
			return new ResponseEntity<>("Crie uma Conta antes de pedir um cartão", HttpStatus.NOT_FOUND);
		}
		
		Cartao cartao;
		
		if(cartaoDto.getTipoDeCartao().equalsIgnoreCase("Credito")) {
			cartao = new CartaoCredito();
		}
		else if (cartaoDto.getTipoDeCartao().equalsIgnoreCase("Debito")) {
			cartao = new CartaoDebito();
		}
		else {
			return new ResponseEntity<>("Selecione um tipo certo de cartão", HttpStatus.NOT_FOUND);
		}
		
		cartao.setConta(contaProcurada.get());
		cartao.setSenha(cartaoDto.getSenha());
		cartao.setNumCartao(cartaoService.gerarNumeroCartao());
		cartao.setStatus(true);
		Cartao cartaoSalvo = cartaoService.addCartao(cartao);
		return new ResponseEntity<>("Cartão do tipo " + cartaoDto.getTipoDeCartao() + " adicionado com sucesso",
				HttpStatus.OK);
	}
	
	@GetMapping("/listAll")
	public ResponseEntity<List<Cartao>> listarCartoes() {
		List<Cartao> cartoes = cartaoService.listarCartoes();
		if(cartoes.isEmpty()) {
			throw new ListaVaziaException("Não foram encontrados cartões");
		}
		return new ResponseEntity<>(cartoes, HttpStatus.FOUND);
	}
}
