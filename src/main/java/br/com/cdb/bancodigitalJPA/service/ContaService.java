package br.com.cdb.bancodigitalJPA.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitalJPA.entity.Cliente;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import br.com.cdb.bancodigitalJPA.entity.ContaCorrente;
import br.com.cdb.bancodigitalJPA.entity.ContaPoupanca;
import br.com.cdb.bancodigitalJPA.exception.ObjetoNuloException;
import br.com.cdb.bancodigitalJPA.exception.QuantidadeExcedidaException;
import br.com.cdb.bancodigitalJPA.exception.SaldoInsuficienteException;
import br.com.cdb.bancodigitalJPA.exception.SubClasseDiferenteException;
import br.com.cdb.bancodigitalJPA.repository.ClienteRepository;
import br.com.cdb.bancodigitalJPA.repository.ContaRepository;
import jakarta.transaction.Transactional;

@Service
public class ContaService {

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	// a conta puxa o cliente, e o cliente puxa o ID

	public Conta addConta(Conta conta) {
		// Recupera o cliente da conta
		Optional<Cliente> clienteEncontrado = clienteRepository.findById(conta.getCliente().getId());

		if (clienteEncontrado.isEmpty()) {
			throw new ObjetoNuloException("Cliente com o ID: " + conta.getCliente().getId() + " não encontrado!");
		}

		Cliente cliente = clienteEncontrado.get();

		if (cliente.getContas() != null && cliente.getContas().size() >= 2) {
			throw new QuantidadeExcedidaException("O cliente já possui duas contas");
		}

		conta.setCliente(cliente);
		return contaRepository.save(conta);
	}

	public Conta removerConta(Long id) {
		Conta contaAchada = buscarContaPorId(id);
		if (contaAchada != null) {
			contaRepository.deleteById(id);
			return contaAchada;
		}
		return null;
	}

	public Conta buscarContaPorId(Long id) {
		return contaRepository.findById(id).orElseThrow(() -> 
			new ObjetoNuloException("Conta não encontrada"));
	}

	public List<Conta> listarContas() {
		return contaRepository.findAll();
	}

	public Double verificarSaldo(Long id) {
		return buscarContaPorId(id).getSaldo();
	}

	@Transactional
	public void transferir(Long origemid, Long destinoid, Double valor) {
		Conta origem = buscarContaPorId(origemid);
		Conta destino = buscarContaPorId(destinoid);

		if (valor > origem.getSaldo()) {
			throw new SaldoInsuficienteException("Saldo insuficiente na conta de origem");
		}

		origem.setSaldo(origem.getSaldo() - valor);
		destino.setSaldo(destino.getSaldo() + valor);

		contaRepository.save(origem);
		contaRepository.save(destino); // atualizando as informações
	}
	// por que usar o @Transactional? 1- ele indica que o método vai ser tratado
	// como transação
	// se no meio do processo algo dá errado, por exemplo em saque, ele não modifica
	// nada do saldo e retorna ao que era antes

	@Transactional
	public void pix(Long id, Double valor) {
		Conta conta = buscarContaPorId(id);
		if (valor > conta.getSaldo()) {
			throw new SaldoInsuficienteException("Saldo insuficiente para fazer o pix");
		}
		conta.setSaldo(conta.getSaldo() - valor);
		contaRepository.save(conta);
	}

	@Transactional
	public void deposito(Long id, Double valor) {
		Conta conta = buscarContaPorId(id);
		conta.setSaldo(conta.getSaldo() + valor);
		contaRepository.save(conta);
	}

	@Transactional
	public void saque(Long id, Double valor) {
		Conta conta = buscarContaPorId(id);
		if (valor > conta.getSaldo()) {
			throw new SaldoInsuficienteException("Saldo insuficiente para saque");
		}
		conta.setSaldo(conta.getSaldo() - valor);
		contaRepository.save(conta);
	}

	@Transactional
	public void aplicarTaxaManutencao(Long id) {
		Conta conta = buscarContaPorId(id);

		if (conta instanceof ContaPoupanca) {
			throw new SubClasseDiferenteException("A taxa de manutenção só pode ser aplicada para contas correntes");
		}
		ContaCorrente contaC = (ContaCorrente) conta; // aqui eu faço o cast pra converter tipos de objeto
		// se eu não faço o Cast, eu teria que instanciar a classe de novo, o que não é
		// uma boa prática
		double taxa = contaC.getTaxaManutencao();

		if (taxa > contaC.getSaldo()) {
			throw new SaldoInsuficienteException("Saldo insuficiente para aplicar taxa");
		}

		contaC.setSaldo(contaC.getSaldo() - taxa);
		contaRepository.save(contaC);
	}

	@Transactional
	public void aplicarRendimento(Long id) {
		Conta conta = buscarContaPorId(id);

		if (conta instanceof ContaCorrente) {
			throw new SubClasseDiferenteException("Rendimento só pode ser aplicado a contas poupanças");
		}

		ContaPoupanca contaP = (ContaPoupanca) conta;
		double taxa = contaP.getTaxaRendimento();

		if (conta.getSaldo() == 0) {
			throw new SaldoInsuficienteException("Não é possível aplicar rendimento a um saldo nulo");
		}

		contaP.setSaldo(contaP.getSaldo() * (1 + taxa));
		contaRepository.save(contaP);
	}

}
