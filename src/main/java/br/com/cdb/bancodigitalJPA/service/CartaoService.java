package br.com.cdb.bancodigitalJPA.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.cdb.bancodigitalJPA.entity.Cartao;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import br.com.cdb.bancodigitalJPA.exception.ObjetoNuloException;
import br.com.cdb.bancodigitalJPA.exception.QuantidadeExcedidaException;
import br.com.cdb.bancodigitalJPA.repository.CartaoRepository;
import br.com.cdb.bancodigitalJPA.repository.ContaRepository;

@Service
public class CartaoService {

	@Autowired
	private CartaoRepository cartaoRepository;

	@Autowired
	private ContaRepository contaRepository;

	private static final int QntdsNum = 15;
	private SecureRandom random = new SecureRandom(); // secureRandom pra gerar os números aleatórios

	public Cartao addCartao(Cartao cartao) {
		Optional<Conta> contaEncontrada = contaRepository.findById(cartao.getConta().getId());

		if (contaEncontrada.isEmpty()) {
			throw new ObjetoNuloException("Conta não encontrada");
		}

		Conta conta = contaEncontrada.get();

		if (conta.getCartoes().size() >= 2) {
			throw new QuantidadeExcedidaException("O cliente já possui duas contas");
		}

		cartao.setConta(conta);
		return cartaoRepository.save(cartao);
	}

	public List<Cartao> listarCartoes() {
		return cartaoRepository.findAll();
	}

	public String gerarNumeroCartao() {
		String num = "";

		for (int i = 0; i < QntdsNum; i++) {
			num += random.nextInt(10);
		}
		int digitoVerificador = calcularDigitoVerificador(num);
		return num + digitoVerificador;
	}

	// Algoritmo de Luhn

	private int calcularDigitoVerificador(String num) {
		int soma = 0;
		boolean dobrar = true; // é tipo pegar os ingredientes e somar eles pra ver se tem o suficiente
		// para fazer um bolo...???

		for (int i = num.length() - 1; i >= 0; i--) {
			int numero = Character.getNumericValue(num.charAt(i));

			if (dobrar) {
				numero *= 2;
				if (numero > 9) {
					numero -= 9;
				}
			}
			soma += numero;
			dobrar = !dobrar; // ele inverte o valor do dobrar, ai ele automaticamente vai dobrar um sim,
								// outro não
			// ele começa true, então ele já começa dobrando o último número
		}
		return (10 - (soma % 10));
		// pega o resto da divisão da soma no (soma % 10) e o primeiro 10 subtrai pra
		// ficar o número certo
	}
	// pegar 15 números gerados e você vai do último número até o primeiro, a cada
	// digito, dobra um
	// e não dobra o próximo, se o número dobrado for maior que 9, subtrai 9 dele
	// soma o número (independente se for subtraido ou não), e repete esse ciclo
	// o último digito é a quantidade que falta até entrar na casa do 0

}
