package br.com.cdb.bancodigitalJPA.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.cdb.bancodigitalJPA.entity.Cartao;
import br.com.cdb.bancodigitalJPA.entity.CartaoCredito;
import br.com.cdb.bancodigitalJPA.entity.CartaoDebito;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import br.com.cdb.bancodigitalJPA.exception.ObjetoNuloException;
import br.com.cdb.bancodigitalJPA.exception.QuantidadeExcedidaException;
import br.com.cdb.bancodigitalJPA.exception.SaldoInsuficienteException;
import br.com.cdb.bancodigitalJPA.exception.StatusNegadoException;
import br.com.cdb.bancodigitalJPA.exception.SubClasseDiferenteException;
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
	
	public Cartao desativarCartao(Long id) {
		Optional<Cartao> cartaoEncontrado = cartaoRepository.findById(id);
		Cartao cartao = cartaoEncontrado.get();
		if(!cartao.isStatus()) {
			throw new StatusNegadoException("Seu cartão já está desativado!");
		}
		cartao.setStatus(false);
		return cartaoRepository.save(cartao);
	}
	
	public Cartao ativarCartao(Long id) {
		Optional<Cartao> cartaoEncontrado = cartaoRepository.findById(id);
		Cartao cartao = cartaoEncontrado.get();
		if(cartao.isStatus()) {
			throw new StatusNegadoException("Seu cartão já está ativado!");
		}
		cartao.setStatus(true);
		return cartaoRepository.save(cartao);
	}
	
	public void realizarPagamento(Long id, double valor) {
		Optional<Cartao> cartaoEncontrado = cartaoRepository.findById(id);
		
		if(cartaoEncontrado.isEmpty()) {
			throw new ObjetoNuloException("Cartão não encontrado");
		}
		
		if(valor < 0) {
			throw new StatusNegadoException("Não é possível realizar o pagamento de um valor menor que 0");
		}
		
		Cartao cartao = cartaoEncontrado.get();
		
		if(!cartao.isStatus()) {
			throw new StatusNegadoException("Seu cartão está desativado, ative-o para continuar");
		}
		
		if(cartao instanceof CartaoDebito) {
			
			CartaoDebito cartaoD = (CartaoDebito) cartao;
			
			if(cartaoD.getLimiteDiario() < valor) {
				throw new SaldoInsuficienteException("O valor excede o limite diário!");
			}
			
			cartaoD.setLimiteDiario(cartaoD.getLimiteDiario() - valor);
		}
		
		else if(cartao instanceof CartaoCredito) {
			CartaoCredito cartaoC = (CartaoCredito) cartao;
			
			if(cartaoC.getFatura() + valor > cartaoC.getLimiteCredito()) {
				throw new SaldoInsuficienteException("Valor ultrapassa o limite de crédito, pague a fatura ou aumente o limite!");
			}
			
			cartaoC.setFatura(cartaoC.getFatura() + valor);
		}
		
		cartaoRepository.save(cartao);
	}
	
	public void pagarFatura(Long id, Double valor) {
		Optional<Cartao> cartaoEncontrado = cartaoRepository.findById(id);
		
		if(cartaoEncontrado.isEmpty()) {
			throw new ObjetoNuloException("Cartão não encontrado");
		}
		
		Cartao cartao = cartaoEncontrado.get();
		Conta conta = cartao.getConta();
		
		if(cartao instanceof CartaoDebito) {
			throw new SubClasseDiferenteException("Cartão de débito não possuí fatura, fique tranquilo");
		}
		
		CartaoCredito cartaoC = (CartaoCredito) cartao;
		
		if(cartaoC.getFatura() == 0) {
			throw new ObjetoNuloException("Não há fatura a ser paga");
		}
		
		if(valor <= 0) {
			throw new ObjetoNuloException("Não é possível pagar com o valor abaixo de 0...");
		}
		
		if(valor > cartaoC.getFatura()) {
			valor = cartaoC.getFatura();
		}
		
		if(conta.getSaldo() < valor) {
			throw new SaldoInsuficienteException("Saldo da conta insuficiente para pagar a fatura");
		}
		
		conta.setSaldo(conta.getSaldo() - valor);
		cartaoC.setFatura(cartaoC.getFatura() - valor);
		cartaoRepository.save(cartaoC);
		
	}
	
	public Cartao buscarCartaoPorId(Long id) {
		return cartaoRepository.findById(id).orElseThrow(() ->
				new ObjetoNuloException("Cartão não encontrado"));
	}
	
	public void alterarLimiteDiario(Long id, double novoLimite) {
		Optional<Cartao> cartaoEncontrado = cartaoRepository.findById(id);
		
		if(cartaoEncontrado.isEmpty()) {
			throw new ObjetoNuloException("Cartão não encontrado!");
		}
		
		Cartao cartao = cartaoEncontrado.get();
		
		if(!cartao.isStatus()) {
			throw new StatusNegadoException("Seu cartão está desativado, ative-o para continuar");
		}
		
		if(cartao instanceof CartaoCredito) {
			throw new SubClasseDiferenteException("Opção indisponível para cartão de crédito");
		}
		CartaoDebito cartaoD = (CartaoDebito) cartao;
		cartaoD.setLimiteDiario(novoLimite);
		cartaoRepository.save(cartaoD);
	}
	
	public void alterarLimiteCredito(Long id, double novoLimite) {
		Optional<Cartao> cartaoEncontrado = cartaoRepository.findById(id);
		
		if(cartaoEncontrado.isEmpty()) {
			throw new ObjetoNuloException("Cartão não encontrado!");
		}
		
		Cartao cartao = cartaoEncontrado.get();
		
		if(!cartao.isStatus()) {
			throw new StatusNegadoException("Seu cartão está desativado, ative-o para continuar");
		}
		
		if(cartao instanceof CartaoDebito) {
			throw new SubClasseDiferenteException("Opção indisponível para cartão de débito");
		}
		CartaoCredito cartaoC = (CartaoCredito) cartao;
		cartaoC.setLimiteCredito(novoLimite);
		cartaoRepository.save(cartaoC);
	}
	
	public double verificarFatura(Long id) {
		Optional<Cartao> cartaoEncontrado = cartaoRepository.findById(id);
		
		if(cartaoEncontrado.isEmpty()) {
			throw new ObjetoNuloException("Cartão não encontrado!");
		}
		
		Cartao cartao = cartaoEncontrado.get();
		
		if(cartao instanceof CartaoDebito) {
			throw new SubClasseDiferenteException("Cartão de Débito não possuí fatura!");
		}
		
		CartaoCredito cartaoC = (CartaoCredito) cartao;
		return cartaoC.getFatura();
	}
	
	public void alterarSenha(Long id, String senhaAntiga, String novaSenha) {
		Optional<Cartao> cartaoEncontrado = cartaoRepository.findById(id);
		Cartao cartao = cartaoEncontrado.get();
		
		if(cartaoEncontrado.isEmpty()) {
			throw new ObjetoNuloException("Cartão não encontrado");
		}
		
		if(!cartao.isStatus()) {
			throw new StatusNegadoException("Seu cartão está desativado, ative-o para continuar");
		}
		
		if(!senhaAntiga.equals(cartao.getSenha())) {
			throw new StatusNegadoException("Senha antiga inválida!");
		} //sempre usa o equals pra comparar o conteúdo de strings!
		
		if(novaSenha.isEmpty()) {
			throw new ObjetoNuloException("A nova senha não pode ser nula");
		}
		
		if(novaSenha.length() < 8) {
			throw new StatusNegadoException("Sua nova senha precisa ter mais de 8 caracteres!");
		}
		
		boolean maiuscula = false;
		boolean digito = false;
		boolean caracterEspecial = false;
		String especial = "!_*@#-";
		//eu converto a novaSenha em um array pra conseguir verificar cada coisa que o usuário colocou na senha
		//eu só preciso de uma maiuscula, um número e um especial, então olho todos e caso ALGUM TENHA, eu prossigo
		for (char c : novaSenha.toCharArray()) {
		    if (Character.isUpperCase(c)) {
		        maiuscula = true;
		    }
		    if (Character.isDigit(c)) {
		        digito = true;
		    }
		    if (especial.indexOf(c) >= 0) { // Se encontrar o caractere na string especiais
		        caracterEspecial = true;
		    }
		}

		if (!maiuscula || !digito || !caracterEspecial) {
		    throw new StatusNegadoException("Sua nova senha precisa conter pelo menos uma letra maiúscula, um número e um caracter especial (- _ ! * @ #)");
		}
		
		cartao.setSenha(novaSenha);
		cartaoRepository.save(cartao);
	}

	public String gerarNumeroCartao() {
		String num = "";

		for (int i = 0; i < QntdsNum; i++) {
			num += random.nextInt(9);
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
