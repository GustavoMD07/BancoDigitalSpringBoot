package br.com.cdb.bancodigitalJPA.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

@Validated
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
		
		if(cartaoDto.getTipoDeCartao().equalsIgnoreCase("Credito?????")) {
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
		cartaoService.addCartao(cartao);
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
	
	@GetMapping("/list/{id}")
	public ResponseEntity<Cartao> buscarCartao(@PathVariable Long id) {
		Cartao cartao = cartaoService.buscarCartaoPorId(id);
		return new ResponseEntity<>(cartao, HttpStatus.FOUND);
	}
	
	@PutMapping("/desativar/{id}")
	public ResponseEntity<String> desativarCartao(@PathVariable Long id) {
		cartaoService.desativarCartao(id);
		
		return new ResponseEntity<>("Cartão desativado com sucesso!", HttpStatus.OK);
	}
	
	@PutMapping("/ativar/{id}")
	public ResponseEntity<String> ativarCartao(@PathVariable Long id) {
		cartaoService.ativarCartao(id);
		return new ResponseEntity<>("Cartão ativado com sucesso!", HttpStatus.OK);
	}
	
	@PutMapping("/limite/{id}")
	public ResponseEntity<String> alterarLimiteCredito(@PathVariable Long id, @RequestParam double novoLimite) {
		cartaoService.alterarLimiteCredito(id, novoLimite);
		return new ResponseEntity<>("Novo limite: R$" + novoLimite, HttpStatus.OK);
	}
	
	@PutMapping("limite-diario/{id}")
	public ResponseEntity<String> alterarLimiteDiario(@PathVariable Long id, @RequestParam double novoLimite) {
		cartaoService.alterarLimiteDiario(id, novoLimite);
		return new ResponseEntity<>("Novo limite diário: R$ " + novoLimite, HttpStatus.OK);
	}
	
	@PutMapping("/senha/{id}")
	public ResponseEntity<String> alterarSenha(@PathVariable Long id, @RequestParam String senhaAntiga, @RequestParam String novaSenha) {
		cartaoService.alterarSenha(id, senhaAntiga, novaSenha);
		return new ResponseEntity<>("Nova senha: " + novaSenha, HttpStatus.OK);
	}
	
	@GetMapping("/fatura/{id}")
	public ResponseEntity<String> verificarFatura(@PathVariable Long id) {
		Cartao cartao = cartaoService.buscarCartaoPorId(id);
		
		if(cartao instanceof CartaoCredito) {
			CartaoCredito cartaoC = (CartaoCredito) cartao;
			return new ResponseEntity<>("Fatura: R$" + cartaoService.verificarFatura(id) + "\nATENÇÃO: Pague sua fatura quando chegar ao limite"
					+ "\n Limite de Crédito: R$" + cartaoC.getLimiteCredito(), HttpStatus.OK );
		}
		
		else {
			return new ResponseEntity<>("Não foi possível verificar a fatura", HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@PostMapping("/pagamento/{id}")
	public ResponseEntity<String> realizarPagamento(@PathVariable Long id, @RequestParam double valor) {
		Cartao cartao = cartaoService.buscarCartaoPorId(id);
		cartaoService.realizarPagamento(id, valor);
		
		if(cartao instanceof CartaoCredito) {
			CartaoCredito cartaoC = (CartaoCredito) cartao;
			return new ResponseEntity<>("Pagamento de valor: R$" + valor + " realizado\n fatura: R$" + 
			cartaoC.getFatura(), HttpStatus.OK);
		}
		
		else if (cartao instanceof CartaoDebito) {
			CartaoDebito cartaoD = (CartaoDebito) cartao;
			return new ResponseEntity<>("Pagamento de valor: R$" + valor + "realizado\n limite diário disponível: R$" + 
			cartaoD.getLimiteDiario(), HttpStatus.OK);
		}
		return new ResponseEntity<>("Não foi possível realizar o pagamento", HttpStatus.NOT_ACCEPTABLE);
	}
	
	@PostMapping("/pagamento/fatura/{id}")
	public ResponseEntity<String> pagarFatura(@PathVariable Long id, @RequestParam double valor) {
		Cartao cartao = cartaoService.buscarCartaoPorId(id);
		Conta conta = cartao.getConta();
		cartaoService.pagarFatura(id, valor);
		if(cartao instanceof CartaoCredito) {
			CartaoCredito cartaoC = (CartaoCredito) cartao;
			return new ResponseEntity<>("Fatura atualizada. Pagamento feito retirando saldo da conta. \nNova fatura: R$" + cartaoC.getFatura() +
			"\n Saldo da conta: R$" + conta.getSaldo(), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>("Não foi possível pagar a fatura", HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
}
